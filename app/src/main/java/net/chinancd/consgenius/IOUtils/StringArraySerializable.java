package net.chinancd.consgenius.IOUtils;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**用于将字符串数组序列化,继承ArrayList,实现KvmSerializable
 * 在WebServiceUtil中进行了下述调用,未能实现上传StringArray
 * StringArraySerializable stringArray=new StringArraySerializable();
 for (int i=0;i<results.length;i++){
 stringArray.add(results[i]);
 }
    soapObject.addProperty("s",stringArray);
 * Created by hedefu
 * on 2016 0413 at 14:15 .
 * email:hedefu999@gmail.com
 */
public class StringArraySerializable extends ArrayList<String> implements KvmSerializable {
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private String namespace="http://222.192.61.8:8889/UploadImage.asmx";


    @Override
    public Object getProperty(int i) {//像ArrayList一样获取元素
        return this.get(i);
    }

    @Override
    public int getPropertyCount() {
        return this.size();
    }

    @Override
    public void setProperty(int i, Object o) {
        this.add(o.toString());
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        propertyInfo.setName("s");
        propertyInfo.type=PropertyInfo.STRING_CLASS;
        propertyInfo.setNamespace(namespace);
    }
}
