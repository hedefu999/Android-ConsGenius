package net.chinancd.consgenius;

import android.app.Application;

/**
 * Created by hedefu
 * on 2016 0411 at 16:38 .
 * email:hedefu999@gmail.com
 */
public class GlobalData extends Application {

    private String ID;
    private String name;
    private int gender = 0;//默认为0，未设定性别,1为成年女，2为成年男，3为小孩。
    private int age;
    private int height;
    private float weight;
    private int[] testans;

    public void onCreate() {
        testans = new int[61];
        for (int i = 0; i < testans.length; i++)
            testans[i] = 0;
        super.onCreate();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        if(ID.length()==18){
            this.ID = ID;
        }else {

        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getTestans(int order) {
        if(order<61){
            return testans[order];
        }else {
            return 0;
        }

    }

    public void setTestans(int order, int value) {
        if(order<61){
            this.testans[order] = value;
        }else {
            //自娱自乐去吧
        }

    }


}