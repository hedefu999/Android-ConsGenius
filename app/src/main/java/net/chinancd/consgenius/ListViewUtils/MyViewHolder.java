package net.chinancd.consgenius.ListViewUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.chinancd.consgenius.R;

/**
 * Created by hedefu
 * on 2016 0411 at 17:00 .
 * email:hedefu999@gmail.com
 */
public class MyViewHolder {
    LinearLayout container;
    TextView questions;
    RadioGroup radioGroup;
    RadioButton rb1_nev;
    RadioButton rb2_sel;
    RadioButton rb3_som;
    RadioButton rb4_oft;
    RadioButton rb5_alw;

    MyViewHolder(View convertView) {
        container = (LinearLayout) convertView.findViewById(R.id.listunit_container);
        questions = (TextView) convertView.findViewById(R.id.questions);
        radioGroup = (RadioGroup) convertView.findViewById(R.id.radiogroup);
        rb1_nev = (RadioButton) convertView.findViewById(R.id.rb1);
        rb2_sel = (RadioButton) convertView.findViewById(R.id.rb2);
        rb3_som = (RadioButton) convertView.findViewById(R.id.rb3);
        rb4_oft = (RadioButton) convertView.findViewById(R.id.rb4);
        rb5_alw = (RadioButton) convertView.findViewById(R.id.rb5);
    }
}
