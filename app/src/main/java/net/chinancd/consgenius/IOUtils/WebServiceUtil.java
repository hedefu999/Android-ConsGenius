package net.chinancd.consgenius.IOUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.chinancd.consgenius.Activities.ConsTestResults;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by hedefu
 * on 2016 0412 at 16:59 .
 * email:hedefu999@gmail.com
 */
public class WebServiceUtil {

    private static final String TAG = "WebServiceUtils";

    /**
     * 该方法扩展时应将参数放在map中,并加入methodname
     * 由于很多变量要在新线程中访问,所以声明为final
     * 传输的内容不限于String
     *
     * @param url                提供服务的asmx
     * @param nameSpace          页面提供
     * @param methodName         页面提供
     * @param webServiceCallBack 回调,要比开启新线程更为合适,便于更新界面
     */
    public static void callWebservice(String url, String nameSpace, String methodName, String[] s,
                                      final WebServiceCallBack webServiceCallBack) {
        final String soapAction = nameSpace + methodName;

        SoapObject transSoap=new SoapObject(nameSpace,methodName);

        //4.使用ArrayList...Http 400
        List<String> transList=new ArrayList<String>();
        for(int i=0;i<s.length;i++){
            transList.add(s[i]);
        }
        SoapObject subSoap=new SoapObject(nameSpace,"s");
        for(String str:transList){
            subSoap.addProperty("",str);
        }
        transSoap.addSoapObject(subSoap);

        //2.使用自定义序列化对象,返回http 400
        /*TransObjSerial strarray=new TransObjSerial();
        strarray.setStrArray(s);
        PropertyInfo propertyInfo=new PropertyInfo();
        propertyInfo.setName("s");
        propertyInfo.setValue(strarray);
        propertyInfo.setType(strarray.getClass());
        propertyInfo.setNamespace(nameSpace);
        transSoap.addProperty(propertyInfo);*/

        /*//1.数组直传,服务端认为数据错误返回http 400
        SoapObject subObject=new SoapObject(nameSpace,"s");
        for(int i=0;i<s.length;i++){
            subObject.addProperty(""+i,s[i]);
        }
        soapObject.addSoapObject(subObject);*/


        //3......
        /*SoapObject soapObject = new SoapObject(nameSpace, methodName);
        PropertyInfo propertyInfo = new PropertyInfo();
        for (int i = 0; i < s.length; i++) {
            propertyInfo.setNamespace(ConsTestResults.nameSpace);
            propertyInfo.setName("String");
            propertyInfo.setValue(s[i]);
        }
        soapObject.addProperty("s", propertyInfo);*/

        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(transSoap);//等于envelope.bodyOut=soapObject;
        //envelope.setOutputSoapObject(transSoap);
        //envelope.addMapping(nameSpace,"TransObjSerial",TransObjSerial.class);

        final HttpTransportSE httpTransSE = new HttpTransportSE(url);
        httpTransSE.debug = true;

        final Handler solveAfterTransHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                webServiceCallBack.callBack((SoapObject) msg.obj);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "数据开始上传");
                SoapObject resObj = null;
                try {
                    httpTransSE.call(soapAction, envelope);
                    Log.e(TAG, "数据上传完成");
                    System.out.println(httpTransSE.requestDump);
                    System.out.println(httpTransSE.responseDump);
                    if (envelope.getResponse() != null) {
                        resObj = (SoapObject) envelope.bodyIn;
                    } else {
                        Log.e(TAG, "获取内容为空！");//网络不通
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.e(TAG, "无法序列化上传的数据");
                } finally {
                    //只返回一条消息
                    solveAfterTransHandler.sendMessage(
                            solveAfterTransHandler.obtainMessage(1, resObj));
                }
            }
        }).start();
    }

    public interface WebServiceCallBack {
        void callBack(SoapObject resultObj);
    }
}
