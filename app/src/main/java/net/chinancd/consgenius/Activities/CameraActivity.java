package net.chinancd.consgenius.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.chinancd.consgenius.CameraUtils.CameraPictureCallback;
import net.chinancd.consgenius.CameraUtils.CameraSizeComparator;
import net.chinancd.consgenius.CameraUtils.FaceDetection;
import net.chinancd.consgenius.IOUtils.CameraTextureView;
import net.chinancd.consgenius.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hedefu
 * on 2016 0411 at 16:51 .
 * email:hedefu999@gmail.com
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "CameraActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera.Parameters mParameters;
    private List<Camera.Size> mPictureSizeList;
    private List<Camera.Size> mPreviewSizeList;
    private List<String> mColorEffectsList;//来玩玩特效
    private Camera.Size properPictureSize;
    private Camera.Size properPreviewSize;
    private CameraSizeComparator sizeComparator;
    private static Camera mCamera;
    private FaceDetection mFaceDection;
    private static Camera.PictureCallback mPictureCallback;
    private Button bn_capture;
    private Button bn_switch;
    private ImageView iv_spin;
    private Animation anim;
    private static TextView mTextView;
    private static int cameraFlag = 1;//1代表前置摄像头，0代表后置,必须设置为static变量否则无法切换摄像头
    private static MediaPlayer mPlayer;
    private static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreated");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera_activity_surface);//不可放置于findViewById之后
        if (checkCameraHardware()) {
            mCamera = getCameraInstance();
        } else {
            showToast(getApplicationContext(), "camera not installed or in use!");
        }
        if (mCamera == null) {
            Log.e(TAG, "oncreate camera==null");
        }

        //控件
        mTextView = (TextView) findViewById(R.id.face);
        bn_capture = (Button) findViewById(R.id.button_capture);
        bn_switch = (Button) findViewById(R.id.button_switch);
        iv_spin = (ImageView) findViewById(R.id.image_scanner);
        anim = AnimationUtils.loadAnimation(this, R.anim.spining_item);
        iv_spin.startAnimation(anim);
        bn_capture.setOnClickListener(clickListener);
        bn_switch.setOnClickListener(clickListener);

        //相机初始化
        mFaceDection = new FaceDetection();
        mPictureCallback = new CameraPictureCallback(this);
        sizeComparator = new CameraSizeComparator();
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPlayer = new MediaPlayer();
        mPlayer = MediaPlayer.create(this, R.raw.hold_out);

        mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    Bundle bundle=new Bundle();
                    bundle.putString("key","你好!");
                    finish();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        Log.e(TAG, " onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, " onResume");
        super.onResume();
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, " onDestroy");
        super.onDestroy();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);//防止出现camera service cannot get
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
            Log.e(TAG, "camera released!");
        }
        mPlayer.release();
    }

    //执行次序：按下电源键在点亮屏幕：onPause->onResume
//来电话或是打开QQ消息通知：onPause->->onResume
//按下home键:onPause->surfaceDestroyed(再打开)
//按下返回键：onPause->surfaceDestroyed->onDestroy
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
        //surfacechanged传进来的width，height指预览界面的长宽(横屏时width大于height)，全屏时刚好是屏幕尺寸
        //mHolder.setFixedSize(640,854);
        startPreview();
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    //app一打开立即success
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private boolean checkCameraHardware() {   //看看有没有摄像头
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance() {   //开始先打开前置摄像头
        Camera camera = null;
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = Camera.open(i);
            }
        }
        return camera;
    }

    public void startPreview() {             //固定竖直方向
        mParameters = mCamera.getParameters();
        mPictureSizeList = mParameters.getSupportedPictureSizes();
        mPreviewSizeList = mParameters.getSupportedPreviewSizes();
        mColorEffectsList = mParameters.getSupportedColorEffects();
        properPictureSize = getMaxSize(mPictureSizeList);//默认获取的preview尺寸是640*480（TCL）1280*720(nubia)
        properPreviewSize = getProperSize(mPreviewSizeList, mSurfaceView.getHeight(), mSurfaceView.getWidth());//这样传参的原因见方法内
        mParameters.setPictureSize(properPictureSize.width, properPictureSize.height);
        mParameters.setPreviewSize(properPreviewSize.width, properPreviewSize.height);
        //mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(properPreviewSize.width,properPreviewSize.height));
        mParameters.set("orientation", "portrait");
        mCamera.setDisplayOrientation(90);//默认水平，需要设置
        mCamera.setFaceDetectionListener(mFaceDection);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setParameters(mParameters);
            startFaceDetection();
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, " mCamera.setPreviewDisplay(mSurfaceHolder) failed");
            e.printStackTrace();
        }
        showList();
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void onFaceDetection (int num) {
        mTextView.setText("发现人脸" + num);
        if (num == 1) {
            mPlayer.start();//播放声音，请伸长您的舌头
            //mCamera.takePicture(null, null, mPictureCallback);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                }
            },2000);
        }

    }

    public Camera.Size getMaxSize(List<Camera.Size> list) {       //max
        //有两种遍历方法，foreach和iterator
        //int i=list.get(0).width;
        //Size maxSize=list.get(0);

        /*for (Size s : list) {
            if(i<s.width){
                i=s.width;
                properSize=s;
                Log.e(TAG,"Size changed!");
            }
        }*/
        /*for(Iterator iterator=list.iterator();iterator.hasNext();){
            Size temp=(Size)iterator.next();
            if(i<temp.width){
                i=temp.width;
                maxSize=temp;
            }
        }
        return maxSize;*/
        Collections.sort(list, sizeComparator);//对size的width升序排列
        return list.get(list.size() - 1);
    }

    public void startFaceDetection() {     //start face detection only after preview has started.
        if (mParameters.getMaxNumDetectedFaces() > 0) {
            mCamera.startFaceDetection();
        } else {
            showToast(this, "FaceDetection not supported!");
        }
    }

    /**
     * @param list  支持的所有Size
     * @param width ,height ratio =screenwidth/screenheight
     * @return 与ratio差值最小并且尺寸最大的size(相等比例时取最大)
     */
    public Camera.Size getProperSize(List<Camera.Size> list, int width, int height) {    //proper size filtered by screenratio
        Log.e(TAG, " screenwidth " + width + " screenheight " + height);
        Collections.sort(list, sizeComparator);
        Camera.Size tempSize, flagSize = null;
        double ratio = (width > height ? (double) width / height : (double) height / width);
        double sizeRatio, tempRatio = 0.0f;
        double diffRatio = Double.MAX_VALUE;
        //获取的size内width是始终大于height的
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            tempSize = (Camera.Size) i.next();
            if (tempSize.width > width) {
                break;
            }//list中width》height，但由于设置竖屏使得此处width《height
            sizeRatio = (double) tempSize.width / tempSize.height;
            tempRatio = Math.abs(sizeRatio - ratio);
            if (tempRatio <= diffRatio) {
                diffRatio = tempRatio;
                flagSize = tempSize;
            }
        }
        return flagSize;
    }

    private void showList() {               //展示设备支持的像素
        for (int i = 0; i < mPictureSizeList.size(); i++) {
            Camera.Size mPictureSize = mPictureSizeList.get(i);
            Log.e("SUPPORTEDPICTURESIZE", "width:" + mPictureSize.width + "height:" + mPictureSize.height);
        }
        for (int i = 0; i < mPreviewSizeList.size(); i++) {
            Camera.Size mPreviewSize = mPreviewSizeList.get(i);
            Log.e("SUPPORTEDPREVIEWSIZE", "width:" + mPreviewSize.width + "height:" + mPreviewSize.height);
        }
        for (Iterator iterator = mColorEffectsList.iterator(); iterator.hasNext(); ) {
            Log.e("SUPPORTEDCOLOREFFECTS", (String) iterator.next());
        }
        Log.e("CHOOSE PICTURESIZE>>>", "width " + properPictureSize.width + " height " + properPictureSize.height);
        Log.e("CHOOSE PREVIEWSIZE>>", "width " + properPreviewSize.width + " height " + properPreviewSize.height);
    }

    public void switchCamera() {             //切换前后摄像头
        //static int cameraFlag=1;//1代表前置摄像头，0代表后置
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//i=0时获得后置摄像头
            if (cameraFlag == 1) {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Log.e(TAG, " CAMERA_FACING_BACK");
                    releaseCamera();
                    mCamera = Camera.open(i);
                    startPreview();
                    cameraFlag = 0;
                    break;
                }
            } else {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    Log.e(TAG, " CAMERA_FACING_FRONT");
                    releaseCamera();
                    mCamera = Camera.open(i);
                    startPreview();
                    cameraFlag = 1;
                    break;
                }
            }


        }
    }

    private int readImgRotation(String filepath) {
        ExifInterface exifInterface = null;
        int rotation = 0;
        try {
            exifInterface = new ExifInterface(filepath);
            int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                default://result=ORIENTATION_ROTATE_NORMAL
                    rotation = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            rotation = -1;
        }
        return rotation;
    }   //读取jpg附带的信息，总是返回零，无效的方法

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_capture:
                    mCamera.takePicture(null, null, mPictureCallback);//原始图像、缩放和压缩图像和JPG图像只需要jpg
                    break;
                case R.id.button_switch:
                    switchCamera();
                    break;
                default:
                    break;
            }
        }
    };
}
