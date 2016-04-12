package net.chinancd.consgenius.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.chinancd.consgenius.IOUtils.CameraTextureView;
import net.chinancd.consgenius.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hedefu
 * on 2016 0411 at 16:51 .
 * email:hedefu999@gmail.com
 */
public class CameraActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUESR_CODE = 200;
    private CameraTextureView mCameraTextureView;
    private Camera mCamera;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            showToast(getApplicationContext(), "onPictureTaken");
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                showToast(getApplicationContext(), "created files failed");
                Toast.makeText(getApplicationContext(), "create files failed", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_surface);
        mCamera = getCameraInstance();
        mCameraTextureView = new CameraTextureView(this, mCamera);
        mCameraTextureView.setSurfaceTextureListener(mCameraTextureView);
        mCameraTextureView.setAlpha(1.0f);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(mCameraTextureView);
        Button captureButton = (Button) findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    private void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    private File getOutputMediaFile(int type) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)), "tongue");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdir()) {
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("HHMMSS").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Tongue_IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Tongue_REC_" + timeStamp + ".mp4");
            } else {
                return null;
            }
            return mediaFile;
        } else {
            Toast.makeText(this, "SD not mountedSD not mountedSD not mounted", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_SHORT).show();
            } else if (requestCode == RESULT_CANCELED) {
                //nothing
                showToast(this, "别害羞嘛·——·");
            } else {
                showToast(this, "somthingwrong");
            }
        }
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUESR_CODE) {
            if (resultCode == RESULT_OK) {
                showToast(this, "Video saved to:\n" + data.getData());
            } else if (resultCode == RESULT_CANCELED) {
                showToast(this, "cancel");
            } else {
                showToast(this, "something wrong");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
