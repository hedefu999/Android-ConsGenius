package net.chinancd.consgenius.IOUtils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by hedefu
 * on 2016 0411 at 16:57 .
 * email:hedefu999@gmail.com
 */
public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;


    public CameraTextureView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException e) {

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.i("TAG------->>>>>", "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i("TAG------->>>>>", "onSurfaceTextureDestoryed");
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.i("TAG------->>>>>", "onSurfaceTextureUpdated");
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);//原则上认为width<height
    }
    public void setAspectRatio(int width,int height){
        requestLayout();
    }*/
}
