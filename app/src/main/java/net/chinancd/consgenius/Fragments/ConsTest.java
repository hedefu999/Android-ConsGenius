package net.chinancd.consgenius.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.chinancd.consgenius.GlobalData;
import net.chinancd.consgenius.ListViewUtils.ListUnit;
import net.chinancd.consgenius.ListViewUtils.MyBaseAdapter;
import net.chinancd.consgenius.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hedefu
 * on 2016 0411 at 16:41 .
 * email:hedefu999@gmail.com
 */
public class ConsTest extends Fragment {
    GlobalData data;
    private ListView mListView;
    private List<ListUnit> questionlist;
    private String[] questions;

    public void onCreate(Bundle savedInstanceState) {
        data = (GlobalData) getActivity().getApplication();
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View act2view = inflater.inflate(R.layout.constest_frag_layout, null);
        questions = getResources().getStringArray(R.array.questionnaire);
        //用getActivity寻找按钮submit ID，造成fragment界面多次切换后按钮死掉，不起作用
        //原因是actionbar的所有控件归activity管，即便fragment能获得actionbar的控件，但不能实时使用
        //改进方法是在activity设置按钮监听，调用act2（fragment）的方法
        mListView = (ListView) act2view.findViewById(R.id.listview);
        questionlist = getList();
        final MyBaseAdapter adapter = new MyBaseAdapter(this.getActivity(), questionlist);
        mListView.setAdapter(adapter);

        return act2view;
    }

    //为每个单元设置题号,题目,其他默认,特殊内容在下一个方法中逐一设定
    private List<ListUnit> getList() {
        List<ListUnit> list = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            ListUnit listUnit = new ListUnit();
            listUnit.position = i;
            listUnit.question = questions[i];
            setListItem(i, listUnit);
            list.add(listUnit);
        }
        return list;
    }

    //在这里，您可以随意设定每条listview显示的答案，朝向，设置自动选择和问题的内容
    private void setListItem(int order, ListUnit listItem) {
        if (order == 7 || order == 15 || order == 23 || order == 31 || order == 37 || order == 44 || order == 51 || order == 58) {
            listItem.last = true;
        }
        switch (order + 1) {
            case 25://真实题号数
                if (data.getGender() == 2) {
                    listItem.rb_nev = getString(R.string.bmi_result_andult_des_1);
                    listItem.rb_sel = getString(R.string.bmi_result_andult_des_2);
                    listItem.rb_som = getString(R.string.bmi_result_andult_des_3);
                    listItem.rb_oft = getString(R.string.bmi_result_andult_des_4);
                    listItem.rb_alw = getString(R.string.bmi_result_andult_des_5);
                } else {
                    listItem.rb_nev = getString(R.string.bmi_result_cute_des_1);
                    listItem.rb_sel = getString(R.string.bmi_result_cute_des_2);
                    listItem.rb_som = getString(R.string.bmi_result_cute_des_3);
                    listItem.rb_oft = getString(R.string.bmi_result_cute_des_4);
                    listItem.rb_alw = getString(R.string.bmi_result_cute_des_5);
                }
                listItem.orientation = 0;//添加这条语句表示答案内容较长,需要垂直显示radiogroup
                //实现从bmi测试获得测试结果自动设置问题答案
                listItem.answerId = data.getTestans(24);
                break;
            case 26:
                listItem.rb_nev = getString(R.string.henshou);
                listItem.rb_sel = getString(R.string.zhengchang);
                listItem.rb_som = getString(R.string.youdian);
                listItem.rb_oft = getString(R.string.duide);
                listItem.rb_alw = getString(R.string.feichang);
                break;
            case 37:
                listItem.rb_nev = getString(R.string.never);
                listItem.rb_sel = getString(R.string.seldom);
                listItem.rb_som = getString(R.string.sometimes);
                listItem.rb_oft = getString(R.string.often);
                listItem.rb_alw = getString(R.string.always);
                switch (data.getGender()) {
                    case 1:
                        listItem.question = getString(R.string.q37_female);
                        break;
                    case 2:
                        listItem.question = getString(R.string.q37_male);
                        break;
                    case 3:
                        listItem.question = getString(R.string.q37_child);
                        listItem.answerId = 1;
                        break;
                    default:
                        break;
                }
                break;
            case 39:
                listItem.rb_nev = getString(R.string.meiyou);
                listItem.rb_sel = getString(R.string.seldom);
                listItem.rb_som = getString(R.string.youxie);
                listItem.rb_oft = getString(R.string.bushao);
                listItem.rb_alw = getString(R.string.henduo);
                break;
            //下述是逆向计分的题目
            case 60:
                listItem.rb_nev = getString(R.string.buneng);
                listItem.rb_sel = getString(R.string.mianqiang);
                listItem.rb_som = getString(R.string.keyi);
                listItem.rb_oft = getString(R.string.bucuo);
                listItem.rb_alw = getString(R.string.dangran);
                break;
            case 61:
                listItem.rb_nev = getString(R.string.always);
                listItem.rb_sel = getString(R.string.often);
                listItem.rb_som = getString(R.string.sometimes);
                listItem.rb_oft = getString(R.string.seldom);
                listItem.rb_alw = getString(R.string.never);
                break;
            default:
                listItem.rb_nev = getString(R.string.never);
                listItem.rb_sel = getString(R.string.seldom);
                listItem.rb_som = getString(R.string.sometimes);
                listItem.rb_oft = getString(R.string.often);
                listItem.rb_alw = getString(R.string.always);
                break;
        }
    }

    public int CheckAnswer() {
        int i = 0;
        //list.size()从1开始数，list.get(i)从0开始数，最后一次i=list.size(),
        // 不经过for循环内部，不可用别的变量去获取题号，而应将i设为循环外访问。
        for (i = 0; i < questionlist.size(); i++) {
            if (questionlist.get(i).answerId == 0)
                break;
        }
        if (i == questionlist.size()) {
            return 0;
        } else {
            return (i + 1);
        }
    }
}
