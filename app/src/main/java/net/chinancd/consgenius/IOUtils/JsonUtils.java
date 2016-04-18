package net.chinancd.consgenius.IOUtils;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by hedefu
 * on 2016 0418 at 7:27 .
 * email:hedefu999@gmail.com
 */
public class JsonUtils {

    public static String teststr="[{\"name\":\"Michael\",\"age\":20},{\"name\":\"Mike\",\"age\":21}]";

    public static void parseJson(String jsonData){
        StringReader in=new StringReader(jsonData);
        JsonReader reader=new JsonReader(in);
        try {
            reader.beginArray();
            while (reader.hasNext()){
                reader.beginObject();
                while (reader.hasNext()){
                    String tagName=reader.nextName();
                    if (tagName.equals("name")){
                        System.out.println("name>>>"+reader.nextString());
                    }else if(tagName.equals("age")){
                        System.out.println("age>>>"+reader.nextInt());
                    }
                }
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseClassFromJson(String jsonData){
        /*Gson gson=new Gson();
        User user=gson.fromJson(jsonData,User.class);
        Log.e(user.getName(),user.getAge());*/

        /*Type ListType=new TypeToken<LinkedList<User>>(){}.getType();
        Gson gson=new Gson();
        LinkedList<User> users=gson.fromJson(jsonData,listType);
        for(Iterator iterator=users.iterator();iterator.hasNext();){
            User user=(User)iterator.next();
            Log.e(user.getName(),user.getAge());
        }*/
    }
}
