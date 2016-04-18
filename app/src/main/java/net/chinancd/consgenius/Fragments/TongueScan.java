package net.chinancd.consgenius.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import net.chinancd.consgenius.R;

import java.io.File;

/**
 * Created by hedefu
 * on 2016 0411 at 16:41 .
 * email:hedefu999@gmail.com
 */
public class TongueScan extends Fragment {
    public static Button scanbutton;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tongueScanView = inflater.inflate(R.layout.tonguescan_frag_layout, null);
        scanbutton = (Button) tongueScanView.findViewById(R.id.tonguescan_frag_btn);
        imageView = (ImageView) tongueScanView.findViewById(R.id.tonguescan_frag_cartoon_iv);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "chocum/tongueScan");
        if (!imagesFolder.exists()) {
            if (!imagesFolder.mkdir()) {
                Toast.makeText(getActivity(), "creating folder failed", Toast.LENGTH_SHORT).show();
            }
        }
        return tongueScanView;
    }


}