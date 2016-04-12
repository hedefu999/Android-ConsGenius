package net.chinancd.consgenius.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.chinancd.consgenius.Activities.Knowledge.Encyclopedia;
import net.chinancd.consgenius.GlobalData;
import net.chinancd.consgenius.IOUtils.WebServiceUtil;
import net.chinancd.consgenius.R;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**计算最终
 * Created by hedefu
 * on 2016 0411 at 16:42 .
 * email:hedefu999@gmail.com
 */
public class ConsTestResults extends Activity {
    private static final String TAG="ConsTestResults";
    private static final String nameSpace = "http://222.192.61.8:8889/webservices/";
    private static final String url = "http://222.192.61.8:8889/UploadImage.asmx";
    private static final String methodName = "UploadConstitution";
    private int[] results={0,0,0,0,0,0,0,0,0};
    private int[] record={0,1,2,3,4,5,6,7,8};
    private String[] cons;//存储9个体质
    private Button yangxubn;
    private Button yinxubn;
    private Button qixubn;
    private Button tanshibn;
    private Button shirebn;
    private Button xueyubn;
    private Button tebingbn;
    private Button qiyubn;
    private Button pinghebn;
    private TextView textViewdes1;
    private TextView textViewdes2;
    private TextView textViewdes3;
    private TextView textViewdes4;
    GlobalData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data=(GlobalData) getApplication();
        setContentView(R.layout.constest_results_layout);
        setActionBarLayout(R.layout.constest_results_actionbar_layout);

        cons=getResources().getStringArray(R.array.constitutions);
        yangxubn=(Button)findViewById(R.id.constest_results_yangxubn);
        yinxubn=(Button)findViewById(R.id.constest_results_yinxubn);
        qixubn=(Button)findViewById(R.id.constest_results_qixubn);
        tanshibn=(Button)findViewById(R.id.constest_results_tanshibn);
        shirebn=(Button)findViewById(R.id.constest_results_shirebn);
        xueyubn=(Button)findViewById(R.id.constest_results_xueyubn);
        tebingbn=(Button)findViewById(R.id.constest_results_tebingbn);
        qiyubn=(Button)findViewById(R.id.constest_results_qiyubn);
        pinghebn=(Button)findViewById(R.id.constest_results_pinghebn);
        textViewdes1=(TextView)findViewById(R.id.constest_results_des2);
        textViewdes2=(TextView)findViewById(R.id.constest_results_des3);
        textViewdes3=(TextView)findViewById(R.id.constest_results_des4);
        textViewdes4=(TextView)findViewById(R.id.constest_results_des5);

        getResults();
        yangxubn.setText(String.valueOf(results[0]));
        yinxubn.setText(String.valueOf(results[1]));
        qixubn.setText(String.valueOf(results[2]));
        tanshibn.setText(String.valueOf(results[3]));
        shirebn.setText(String.valueOf(results[4]));
        xueyubn.setText(String.valueOf(results[5]));
        tebingbn.setText(String.valueOf(results[6]));
        qiyubn.setText(String.valueOf(results[7]));
        pinghebn.setText(String.valueOf(results[8]));
        sort();
        getdes();
        accessWebService(url,nameSpace,methodName);

    }
    private void getResults(){
        int i=0;
        for(i=0;i<7;i++){results[0]+=data.getTestans(i);}//阳虚质测试共7题
        results[0]=(results[0]-7)*100/28;
        for(i=7;i<15;i++){results[1]+=data.getTestans(i);}
        results[1]=(results[1]-8)*100/32;
        for(i=15;i<23;i++){results[2]+=data.getTestans(i);}
        results[2]=(results[2]-8)*100/32;
        for(i=23;i<31;i++){results[3]+=data.getTestans(i);}
        results[3]=(results[3]-8)*100/32;
        for(i=31;i<37;i++){results[4]+=data.getTestans(i);}
        results[4]=(results[4]-6)*100/24;
        for(i=37;i<44;i++){results[5]+=data.getTestans(i);}
        results[5]=(results[5]-7)*100/28;
        for(i=44;i<51;i++){results[6]+=data.getTestans(i);}
        results[6]=(results[6]-7)*100/28;
        for(i=51;i<58;i++){results[7]+=data.getTestans(i);}
        results[7]=(results[7]-7)*100/28;
        results[8]=data.getTestans(58)+data.getTestans(59)+data.getTestans(60)+(30-data.getTestans(15)-data.getTestans(21)-data.getTestans(51)-data.getTestans(3)-data.getTestans(42));
        results[8]=(results[8]-8)*100/32;
    }
    public void onTileClick(View v){
        startActivity(new Intent(this, Encyclopedia.class));
    }
    public void onActionBarItemClick(View view){finish();}
    private void setActionBarLayout(int layoutid) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layoutid, null);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(view, layoutParams);
        }
    }
    private void sort(){
        int temp=0;
        for(int i=0;i<results.length-1;i++){
            for(int j=i+1;j<results.length;j++){
                if(results[i]<results[j]){
                    if(results[i]==results[j]){
                        Toast.makeText(this,"存在体质得分相等的情况。。。",Toast.LENGTH_SHORT).show();
                    }
                    temp=results[i];
                    results[i]=results[j];
                    results[j]=temp;
                    temp=record[i];
                    record[i]=record[j];
                    record[j]=temp;
                }
            }
        }
    }
    private void getdes(){
        //第一种情况偏颇体质得分超过平和质，直接判定体质为得分最高的偏颇体质
        if(record[0]!=8){
            textViewdes1.setText(R.string.constest_results_des_2);
            textViewdes2.setText(cons[record[0]]);
            //第二种情况，在平和质分数最高的情况下，次高分》=40分，应判定为该体质
        }else if(results[1]>39){
            textViewdes1.setText(R.string.constest_results_des_3);
            textViewdes2.setText(cons[record[1]]);
            //第三种情况，次高分3X，偏向于该体质
        }else if(results[1]>29){
            textViewdes1.setText(R.string.constest_results_des_4);
            textViewdes2.setText(cons[8]);
            textViewdes3.setText(R.string.constest_results_des_7);
            textViewdes4.setText(cons[record[1]]);
            //正常体质
        }else{
            textViewdes1.setText(R.string.constest_results_des_5);
            textViewdes2.setText(cons[8]);
        }
    }
    private void accessWebService(String url, String nameSpace, String methodName){
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("id_card_number",data.getID());
        params.put("s","主要数据");
        WebServiceUtil.callWebservice(url, nameSpace, methodName, params, new WebServiceUtil.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject resultObj) {
                if(resultObj!=null){
                    Toast.makeText(getApplicationContext(),"中康承诺保护您的隐私,呵呵!",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"返回结果="+resultObj.toString());
                    StringBuilder sb=new StringBuilder();
                    for(int i=0;i<resultObj.getPropertyCount();i++){
                        sb.append(resultObj.getProperty(i)).append("\r\n");
                    }
                    String result=sb.toString();
                    Log.e(TAG,"返回结果="+result);
                }
            }
        });
    }
}
