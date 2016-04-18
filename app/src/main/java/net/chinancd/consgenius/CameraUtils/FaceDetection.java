package net.chinancd.consgenius.CameraUtils;

import android.hardware.Camera;

import net.chinancd.consgenius.Activities.CameraActivity;

/**
 * Created by hedefu
 * on 2016 0416 at 8:46 .
 * email:hedefu999@gmail.com
 */
public class FaceDetection implements Camera.FaceDetectionListener{
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        CameraActivity.onFaceDetection(faces.length);
    }
}
