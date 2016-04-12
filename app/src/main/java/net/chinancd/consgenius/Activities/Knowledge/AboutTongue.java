package net.chinancd.consgenius.Activities.Knowledge;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import net.chinancd.consgenius.R;

/**
 * Created by hedefu
 * on 2016 0411 at 16:53 .
 * email:hedefu999@gmail.com
 */
public class AboutTongue extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.abouttongue);
        setActionBarLayout(R.layout.abouttongue_actionbar);
    }
    public void setActionBarLayout(int layoutid) {
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
    public void onActionBarItemClick(View view){finish();}
}
