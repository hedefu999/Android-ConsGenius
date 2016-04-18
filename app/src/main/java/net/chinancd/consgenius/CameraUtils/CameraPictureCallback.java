package net.chinancd.consgenius.CameraUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import net.chinancd.consgenius.Activities.Mainactivity;
import net.chinancd.consgenius.Fragments.TongueScan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hedefu
 * on 2016 0416 at 8:41 .
 * email:hedefu999@gmail.com
 */
public class CameraPictureCallback implements Camera.PictureCallback {
    private Context context;
    private String timeStamp;
    private int CLIP_IMAGE_WIDTH, CLIP_IMAGE_HEIGHT = 0;
    private int CLIP_IMAGE_NORTHWEST_X, CLIP_IMAGE_NORTHWEST_Y;

    public CameraPictureCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File photo = getOutputMediaFile();
        if (photo == null) {
            Log.e("CameraPictureCallback", "capture failed!");
            camera.startPreview();
            return;
        }
        //实现对竖屏拍照照片朝向的纠正
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(270);//已旋转
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        CLIP_IMAGE_WIDTH = getClipAreaSize(context).x;
        CLIP_IMAGE_HEIGHT = getClipAreaSize(context).y;
        CLIP_IMAGE_NORTHWEST_X = bitmap.getWidth() - CLIP_IMAGE_WIDTH / 2;
        CLIP_IMAGE_NORTHWEST_Y = bitmap.getHeight() - CLIP_IMAGE_HEIGHT / 2;
        //图片的比例尺寸与屏幕差距很大，在不同手机上肯定导致截取的图片位置不可控制，自动截取不能靠比例像素
        Bitmap clipBitmap = Bitmap.createBitmap(bitmap, CLIP_IMAGE_NORTHWEST_X, CLIP_IMAGE_NORTHWEST_Y,
                CLIP_IMAGE_WIDTH, CLIP_IMAGE_HEIGHT);
        //clipbitmap是剪切图片，bitmap是全幅图片
        try {
            FileOutputStream fos = new FileOutputStream(photo);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private File getOutputMediaFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Create the storage directory if it does not exist
            timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            File mediaFile = new File(Mainactivity.tongueFolder + File.separator + "TongueScan" + timeStamp + ".jpg");
            MediaScannerConnection.scanFile(context,        //一行代码实现更新图库
                    new String[]{mediaFile.getAbsolutePath()}, null, null);
            //此处media.toString(),getAbsolutePath(),getPath()均正常
            return mediaFile;
        } else {
            Log.e("CameraPictureCallback", "SD card not found!");
            return null;
        }
    }

    /**
     * 设备独立像素转像素：dip--->pixel
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public Point getClipAreaSize(Context context) {  //获取舌头截取框占据屏幕的像素尺寸，建议通过preview获取图片进行分析
        Point point = null;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        point.x = displayMetrics.widthPixels / 2;
        point.y = displayMetrics.heightPixels / 3;
        return point;
    }
}
