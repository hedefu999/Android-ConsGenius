package net.chinancd.consgenius.ListViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import net.chinancd.consgenius.GlobalData;
import net.chinancd.consgenius.R;

import java.util.List;

/**
 * Created by hedefu
 * on 2016 0411 at 16:59 .
 * email:hedefu999@gmail.com
 */
public class MyBaseAdapter extends BaseAdapter {
    GlobalData data;
    Context context;
    List<ListUnit> mList = null;
    LayoutInflater mInflater = null;
    int[] listbac = {R.drawable.constest_listunit_bac, R.drawable.constest_listunit_bac_vice};

    public MyBaseAdapter(Context context, List<ListUnit> list) {
        this.context = context;
        data = (GlobalData) context.getApplicationContext();
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.constest_listunit, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        //设置listview背景交替变换
        convertView.setBackgroundResource(listbac[position % 2]);

        holder.questions.setText(mList.get(position).question);
        //这里需要主动设置题目，与答案不同。问题的内容存储在一个个Listitem对象中，而答案是存储在布局文件中的，
        // 两者都可以专门设置，只不过答案是有很多重复的可以自动设置
        //position的数值的混乱造成holder找到的数据是别人的，导致滚动混乱
        holder.rb1_nev.setText(mList.get(position).rb_nev);
        holder.rb2_sel.setText(mList.get(position).rb_sel);
        holder.rb3_som.setText(mList.get(position).rb_som);
        holder.rb4_oft.setText(mList.get(position).rb_oft);
        holder.rb5_alw.setText(mList.get(position).rb_alw);
        holder.radioGroup.setOrientation(
                (mList.get(position).orientation == 0) ? (LinearLayout.VERTICAL) : (LinearLayout.HORIZONTAL));
        holder.radioGroup.setId(position);
        holder.radioGroup.setOnCheckedChangeListener(null);

        //这里设置每组的分割线,每道题的题号与position不是一个概念
        //题号存储在listunit中,作为一个成员变量,要根据该成员变量确定当前操作的是哪一题
        LinearLayout.LayoutParams linearparams;
        if (mList.get(position).last) {
            holder.container.setVisibility(View.VISIBLE);//缺少这句无法加载背景...原因不明
            linearparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 25);
            holder.container.setLayoutParams(linearparams);
            holder.container.setBackgroundResource(R.drawable.constest_listdivider);
        } else {
            holder.container.setVisibility(View.INVISIBLE);
            linearparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.container.setLayoutParams(linearparams);
        }

        //从类Listitem中获取保存的项目状态，用于滚动时恢复item
        switch (mList.get(position).answerId) {
            case 1:
                holder.radioGroup.check(R.id.rb1);
                break;
            case 2:
                holder.radioGroup.check(R.id.rb2);
                break;
            case 3:
                holder.radioGroup.check(R.id.rb3);
                break;
            case 4:
                holder.radioGroup.check(R.id.rb4);
                break;
            case 5:
                holder.radioGroup.check(R.id.rb5);
                break;
            default:
                holder.radioGroup.clearCheck();
                break;
        }

        //用于保存选中的信息，以销毁该item
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ListUnit temp = mList.get(group.getId());
                switch (checkedId) {
                    //data是定义的外部类，专门存储数据，内含testans数组与其set方法
                    //temp的作用未知，无意义
                    //data.setTestans(int i)根据题号设置每个数组项的值以保存
                    case R.id.rb1:
                        temp.answerId = 1;
                        data.setTestans(group.getId(), 1);
                        break;
                    case R.id.rb2:
                        temp.answerId = 2;
                        data.setTestans(group.getId(), 2);
                        break;
                    case R.id.rb3:
                        temp.answerId = 3;
                        data.setTestans(group.getId(), 3);
                        break;
                    case R.id.rb4:
                        temp.answerId = 4;
                        data.setTestans(group.getId(), 4);
                        break;
                    case R.id.rb5:
                        temp.answerId = 5;
                        data.setTestans(group.getId(), 5);
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }
}
