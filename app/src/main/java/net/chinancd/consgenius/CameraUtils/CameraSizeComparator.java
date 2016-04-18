package net.chinancd.consgenius.CameraUtils;

import android.hardware.Camera;

import java.util.Comparator;

/**
 * Created by hedefu
 * on 2016 0416 at 8:44 .
 * email:hedefu999@gmail.com
 */
public class CameraSizeComparator implements Comparator<Camera.Size> {
    @Override
    public int compare(Camera.Size lhs, Camera.Size rhs) {
        if (lhs.width == rhs.width) {
            return 0;
        } else if (lhs.width > rhs.width) {
            return 1;
        } else {
            return -1;
        }

    }
}
