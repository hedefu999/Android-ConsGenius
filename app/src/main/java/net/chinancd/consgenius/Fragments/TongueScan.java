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

    private Button scanbutton;
    private ImageView imageView;

    //private String timeStamp;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View act3view = inflater.inflate(R.layout.tonguescan_frag_layout, null);
        scanbutton = (Button) act3view.findViewById(R.id.tonguescan_frag_btn);
        imageView = (ImageView) act3view.findViewById(R.id.tonguescan_frag_cartoon_iv);
        //timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "/ConstitutionsGenius/tongue");
        if (!imagesFolder.exists()) {
            if (!imagesFolder.mkdir()) {
                Toast.makeText(getActivity(), "creating folder failed", Toast.LENGTH_SHORT).show();
            }
        }
         /*scanbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 startActivity(new Intent(getActivity(),CameraActivity.class));

                *//* Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 File image=new File(imagesFolder,"tongue.jpg");
                 Uri uriSavedImage=Uri.fromFile(image);
                 cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,uriSavedImage);
                 startActivityForResult(cameraintent, codecamera);*//*
             }
         });*/
        return act3view;
    }

    /*public void savebitmap(String bitName) throws IOException{
        File file=new File("/sdcard/ConstitutionsGenius/"+bitName+".png");
        file.createNewFile();
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.print("file write failed");
        }

    }*/
}