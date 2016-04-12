package net.chinancd.consgenius.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.chinancd.consgenius.GlobalData;
import net.chinancd.consgenius.R;

/**
 * Created by hedefu
 * on 2016 0411 at 16:33 .
 * email:hedefu999@gmail.com
 */
public class BMIResults extends DialogFragment {
    private static final String TAG="BMIResults";
    private GlobalData data;//全局数据
    private TextView tv_bmi;
    private TextView tv_des;
    private Button button;
    private Bundle bundle;
    private String ID;
    private String name;
    private int gender=0;
    private int age=0;
    private double height=0;
    private double weight=0;

    public void onCreate(Bundle savedInstanceState) {
        data=(GlobalData)(getActivity().getApplication());
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        bundle=getArguments();

        if(bundle.getString("age")!=null&&bundle.getString("height")!=null&&bundle.getString("weight")!=null){
            ID=bundle.getString("ID");
            name=bundle.getString("name");
            gender=bundle.getInt("gender");
            age=Integer.valueOf(bundle.getString("age"));
            height=Double.valueOf(bundle.getString("height"))/100;
            weight=Double.valueOf(bundle.getString("weight"));
        }else {
            Toast.makeText(getActivity(),"空",Toast.LENGTH_LONG).show();}
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bmi_results_layout,container);
        tv_bmi=(TextView)view.findViewById(R.id.bmi_result);
        tv_des=(TextView)view.findViewById(R.id.bmi_result_des);
        button=(Button)view.findViewById(R.id.bmi_result_btn);
        //在oncreate中调用getbmi函数会引起空指针异常
        final double bmi=getBMI(height, weight);
        getDes(gender, age, bmi);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
    public void onPause() {
        super.onPause();
    }
    public double getBMI(double height,double weight){
        double BMI=0;
        //最后请在这里完善函数gender=1为女，gender=2为男
        BMI=weight/height/height;
        tv_bmi.setText(String.valueOf((int) BMI));
        return BMI;
    }
    public void getDes(int gender,int age,double result){
        data.setName(name);
        data.setID(ID);
        /*Log.e(TAG,"名字是:"+name);
        Log.e(TAG,"身份证号是:"+ID);
        Log.e(TAG,"性别是:"+gender);
        Log.e(TAG,"年龄是:"+age);*/
        if(age<15){data.setGender(3);}else if(gender==1){data.setGender(1);}else if(gender==2){data.setGender(2);}

        if((age>=18)&&(result<18.5)||((age<18)&&(result<15))){
            if((age>=18)&&(gender==2)){
                tv_des.setText(R.string.bmi_result_andult_des_1);
            }else {
                tv_des.setText(R.string.bmi_result_cute_des_1);
            }data.setTestans(24, 1);//3表示bmi问题在第三题
        }else if(((age>=18)&&(result<24.99))||((age==7)&&(((gender==1)&&(result<17.2))||((gender==2)&&(result<17.4))))||
                ((age==8)&&(((gender==1)&&(result<18.1))||((gender==2)&&(result<18.1))))||
                ((age==9)&&(((gender==1)&&(result<19.0))||((gender==2)&&(result<18.9))))||
                ((age==10)&&(((gender==1)&&(result<20.0))||((gender==2)&&(result<19.6))))||
                ((age==11)&&(((gender==1)&&(result<21.1))||((gender==2)&&(result<20.3))))||
                ((age==12)&&(((gender==1)&&(result<21.9))||((gender==2)&&(result<21.0))))||
                ((age==13)&&(((gender==1)&&(result<22.6))||((gender==2)&&(result<21.9))))||
                ((age==14)&&(((gender==1)&&(result<23.0))||((gender==2)&&(result<22.6))))||
                ((age==15)&&(((gender==1)&&(result<23.4))||((gender==2)&&(result<23.1))))||
                ((age==16)&&(((gender==1)&&(result<23.7))||((gender==2)&&(result<23.5))))||
                ((age==17)&&(((gender==1)&&(result<23.8))||((gender==2)&&(result<23.8))))){
            if((age>=18)&&(gender==2)){
                tv_des.setText(R.string.bmi_result_andult_des_2);
            }else {
                tv_des.setText(R.string.bmi_result_cute_des_2);
            }data.setTestans(24,2);
        }else if(((age>=18)&&(result<28))||((age==7)&&(((gender==1)&&(result<18.9))||((gender==2)&&(result<19.2))))||
                ((age==8)&&(((gender==1)&&(result<19.9))||((gender==2)&&(result<20.3))))||
                ((age==9)&&(((gender==1)&&(result<21.0))||((gender==2)&&(result<21.4))))||
                ((age==10)&&(((gender==1)&&(result<22.1))||((gender==2)&&(result<22.5))))||
                ((age==11)&&(((gender==1)&&(result<23.3))||((gender==2)&&(result<23.6))))||
                ((age==12)&&(((gender==1)&&(result<24.5))||((gender==2)&&(result<24.7))))||
                ((age==13)&&(((gender==1)&&(result<25.6))||((gender==2)&&(result<25.7))))||
                ((age==14)&&(((gender==1)&&(result<26.3))||((gender==2)&&(result<26.4))))||
                ((age==15)&&(((gender==1)&&(result<26.9))||((gender==2)&&(result<26.9))))||
                ((age==16)&&(((gender==1)&&(result<27.4))||((gender==2)&&(result<27.4))))||
                ((age==17)&&(((gender==1)&&(result<27.7))||((gender==2)&&(result<27.8))))){
            if((age>=18)&&(gender==2)){
                tv_des.setText(R.string.bmi_result_andult_des_3);
            }else {
                tv_des.setText(R.string.bmi_result_cute_des_3);
            }data.setTestans(24,3);
        }else if((age>=7)&&(result<30)){
            if((age>=18)&&(gender==2)){
                tv_des.setText(R.string.bmi_result_andult_des_4);
            }else {
                tv_des.setText(R.string.bmi_result_cute_des_4);
            }
            data.setTestans(24,4);
        }else{
            if((age>=18)&&(gender==2)){
                tv_des.setText(R.string.bmi_result_andult_des_5);
            }else {
                tv_des.setText(R.string.bmi_result_cute_des_5);
            }
            data.setTestans(24,5);
        }
    }


}
