package net.chinancd.consgenius.IOUtils;

import net.chinancd.consgenius.Activities.ConsTestResults;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by hedefu
 * on 2016 0415 at 8:44 .
 * email:hedefu999@gmail.com
 */
public class TransObjSerial implements KvmSerializable {
    private String namespace="http://222.192.61.8:8889/UploadImage.asmx";
    public String[] getStrArray() {
        return strArray;
    }

    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    private String[] strArray;

    @Override
    public Object getProperty(int i) {
        return strArray[i];
    }

    @Override
    public int getPropertyCount() {
        return strArray.length;
    }

    @Override
    public void setProperty(int i, Object o) {
        strArray[i]=o.toString();
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        propertyInfo.name = ""+i;
        propertyInfo.type = PropertyInfo.STRING_CLASS;
        propertyInfo.namespace = namespace;
    }
}
