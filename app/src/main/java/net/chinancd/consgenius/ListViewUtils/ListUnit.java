package net.chinancd.consgenius.ListViewUtils;

/**
 * Created by hedefu
 * on 2016 0411 at 16:58 .
 * email:hedefu999@gmail.com
 */
public class ListUnit {
    public int position = 0;//position为0表示第一题
    public int orientation = 1;//默认是1--horizontal
    public int answerId = 0; // hold the answer picked by the user, initial is NONE(see below)// No answer selected
    public String rb_nev;
    public String rb_sel;
    public String rb_som;
    public String rb_oft;
    public String rb_alw;
    public String question; // hold the question
    public boolean last = false;//是否是每组最后一题
}
