package net.chinancd.consgenius.CameraUtils;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import net.chinancd.consgenius.Activities.CameraActivity;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hedefu
 * on 2016 0416 at 8:42 .
 * email:hedefu999@gmail.com
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private List<Camera.Size> mPictureSizeList;
    private List<Camera.Size> mPreviewSizeList;
    private List<String> mColorEffectsList;//来玩玩特效
    private Camera.Size properPictureSize;
    private Camera.Size properPreviewSize;
    private CameraSizeComparator sizeComparator;
    private View view;

    public CameraPreview(Context context) {
        super(context);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        /*try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.e(TAG, "surfaceCreated");
        mParameters = mCamera.getParameters();
        mPictureSizeList = mParameters.getSupportedPictureSizes();
        mPreviewSizeList = mParameters.getSupportedPreviewSizes();
        mColorEffectsList = mParameters.getSupportedColorEffects();
        sizeComparator = new CameraSizeComparator();

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            startFaceDetection();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
        //surfacechanged传进来的width，height指预览界面的长宽(横屏时width大于height)，全屏时刚好是屏幕尺寸
        //mHolder.setFixedSize(640,854);
        properPictureSize = getMaxSize(mPictureSizeList);//默认获取的preview尺寸是640*480（TCL）1280*720(nubia)
        properPreviewSize = getProperSize(mPreviewSizeList, width, height);
        showList();
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null) {
            return; // preview surface does not exist
        }
        try {// stop preview before making changes
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        mParameters.setPictureSize(properPictureSize.width, properPictureSize.height);
        mParameters.setPreviewSize(properPreviewSize.width, properPreviewSize.height);
        mParameters.set("orientation", "portrait");
        mCamera.setDisplayOrientation(90);//默认水平，需要设置
        mCamera.setParameters(mParameters);

        try {// start preview with new settings
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            startFaceDetection();
        } catch (Exception e) {
            mCamera.release();
        }

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

    public void startFaceDetection() {     //start face detection only after preview has started.
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getMaxNumDetectedFaces() > 0) {
            mCamera.startFaceDetection();
        } else {
            CameraActivity.showToast(getContext(), "FaceDetection not supported!");
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

    /**
     * @param list  支持的所有Size
     * @param width ,height ratio =screenwidth/screenheight
     * @return 与ratio差值最小并且尺寸最大的size(相等比例时取最大)
     */
    public Camera.Size getProperSize(List<Camera.Size> list, int width, int height) {    //proper size filtered by screenratio
        Collections.sort(list, sizeComparator);
        Camera.Size tempSize, flagSize = null;
        double ratio = (width > height ? (double) width / height : (double) height / width);
        double sizeRatio, tempRatio = 0.0f;
        double diffRatio = Double.MAX_VALUE;
        //获取的size内width是始终大于height的
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            tempSize = (Camera.Size) i.next();
            sizeRatio = (double) tempSize.width / tempSize.height;
            tempRatio = Math.abs(sizeRatio - ratio);
            Log.e(TAG, " " + diffRatio + " ");
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
}
