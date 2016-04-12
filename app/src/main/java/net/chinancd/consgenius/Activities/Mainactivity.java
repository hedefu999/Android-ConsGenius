package net.chinancd.consgenius.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.chinancd.consgenius.Activities.Knowledge.AboutTongue;
import net.chinancd.consgenius.Activities.Knowledge.Encyclopedia;
import net.chinancd.consgenius.Fragments.Accounts;
import net.chinancd.consgenius.Fragments.BMI;
import net.chinancd.consgenius.Fragments.ConsTest;
import net.chinancd.consgenius.Fragments.TongueScan;
import net.chinancd.consgenius.R;

import java.io.File;
import java.util.Timer;

public class Mainactivity extends Activity implements View.OnClickListener {
    private  final String TAG=this.getClass().getName();
    private static boolean isQuit = false;
    private long mExitTime = 0;

    private BMI bmiFrag;
    private ConsTest consTestFrag;
    private TongueScan tongueScanFrag;
    private Accounts accountsFrag;
    private View bmiView;
    private View consTestView;
    private View tongueScanView;
    private View accountsView;
    private ImageView bmiIv;
    private ImageView consTestIv;
    private ImageView tongueScanIv;
    private ImageView accountsIv;
    private TextView bmiTv;
    private TextView consTestTv;
    private TextView tongueScanTv;
    private TextView accountsTv;
    private FragmentManager fragmentMananger;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止软键盘把底下的按钮栏挤上来
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.mainactivity);
        fileManager();
        initViews();
        fragmentMananger = getFragmentManager();
        setTabSelection(1);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1500) {//
                // 如果两次按键时间间隔大于1500毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                finish();
                //System.exit(0);//这种退出方式会闪屏，但是能彻底清除应用的一些数据
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fileManager() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mainFolder = new File(Environment.getExternalStorageDirectory(), "ConstitutionsGenius");
            if (!mainFolder.exists()) {
                if (!mainFolder.mkdir()) {
                    Toast.makeText(this, "creating folder failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initViews() {
        //action1是总布局文件中的相对布局
        bmiView = findViewById(R.id.bmiBottomBtn);
        consTestView = findViewById(R.id.consTestBottomBtn);
        tongueScanView = findViewById(R.id.tongueScanBottomBtn);
        accountsView = findViewById(R.id.accountsBottomBtn);
        bmiIv = (ImageView) findViewById(R.id.bmiIv);
        consTestIv = (ImageView) findViewById(R.id.consTestIv);
        tongueScanIv = (ImageView) findViewById(R.id.tongueScanIv);
        accountsIv = (ImageView) findViewById(R.id.accountsIv);
        bmiTv = (TextView) findViewById(R.id.bmiTv);
        consTestTv = (TextView) findViewById(R.id.consTestTv);
        tongueScanTv = (TextView) findViewById(R.id.tongueScanTv);
        accountsTv = (TextView) findViewById(R.id.accountsTv);
        bmiView.setOnClickListener(this);
        consTestView.setOnClickListener(this);
        tongueScanView.setOnClickListener(this);
        accountsView.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bmiBottomBtn:
                setTabSelection(1);
                break;
            case R.id.consTestBottomBtn:
                setTabSelection(2);
                break;
            case R.id.tongueScanBottomBtn:
                setTabSelection(3);
                break;
            case R.id.accountsBottomBtn:
                setTabSelection(4);
                break;
            case R.id.tonguescan_frag_btn:
                setTabSelection(5);
                break;
            default:
                break;
        }

    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentMananger.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 1:
                bmiIv.setImageResource(R.drawable.gravity_sle);
                bmiTv.setTextColor(Color.WHITE);
                if (bmiFrag == null) {
                    bmiFrag = new BMI();
                    transaction.add(R.id.content, bmiFrag);
                } else {
                    transaction.show(bmiFrag);
                }
                setActionBarLayout(R.layout.bmi_actionbar_layout);
                break;
            case 2:
                consTestIv.setImageResource(R.drawable.cons_sle);
                consTestTv.setTextColor(Color.WHITE);
                if (consTestFrag == null) {
                    consTestFrag = new ConsTest();
                    transaction.add(R.id.content, consTestFrag);
                } else {
                    transaction.show(consTestFrag);
                }
                setActionBarLayout(R.layout.constest_actionbar_layout);
                break;
            case 3:
                tongueScanIv.setImageResource(R.drawable.tongue_sle);
                tongueScanTv.setTextColor(Color.WHITE);
                if (tongueScanFrag == null) {
                    tongueScanFrag = new TongueScan();
                    transaction.add(R.id.content, tongueScanFrag);
                } else {
                    transaction.show(tongueScanFrag);
                }
                setActionBarLayout(R.layout.tonguescan_actionbar_layout);
                break;
            case 4:
                accountsIv.setImageResource(R.drawable.account_sle);
                accountsTv.setTextColor(Color.WHITE);
                if (accountsFrag == null) {
                    accountsFrag = new Accounts();
                    transaction.add(R.id.content, accountsFrag);
                } else {
                    transaction.show(accountsFrag);
                }
                setActionBarLayout(R.layout.accounts_actionbar_layout);
                break;
            default://此处没有default运行出错！
                Toast.makeText(this, "nothing,just for fun", Toast.LENGTH_SHORT).show();
                break;
        }

        transaction.commit();
    }

    private void clearSelection() {
        bmiIv.setImageResource(R.drawable.gravity_com);
        bmiTv.setTextColor(Color.BLACK);
        consTestIv.setImageResource(R.drawable.cons_com);
        consTestTv.setTextColor(Color.BLACK);
        tongueScanIv.setImageResource(R.drawable.tongue_com);
        tongueScanTv.setTextColor(Color.BLACK);
        accountsIv.setImageResource(R.drawable.account_com);
        accountsTv.setTextColor(Color.BLACK);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (bmiFrag != null) {
            transaction.hide(bmiFrag);
        }
        if (consTestFrag != null) {
            transaction.hide(consTestFrag);
        }
        if (tongueScanFrag != null) {
            transaction.hide(tongueScanFrag);
        }
        if (accountsFrag != null) {
            transaction.hide(accountsFrag);
        }
    }

    //functions for actionbar
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

    public void onActionBarItemClickListener(View view) {
        switch (view.getId()) {
            case R.id.tonguescan_actionbar_logo:
                Toast.makeText(this, "你好，我是体质小精灵", Toast.LENGTH_SHORT).show();
                break;
            case R.id.actionbar_title:
                Toast.makeText(this, "话说健康是成功的资本，你说是不？", Toast.LENGTH_LONG).show();
                break;
            case R.id.actionbar_encyclopedia:
                startActivity(new Intent(this, Encyclopedia.class));
                break;
            case R.id.help:
                getSharedPreferences("whatsnew", 0).edit().putInt("num", 2).commit();
                startActivity(new Intent(this, Welcome.class));
                break;
            case R.id.tonguescan_actionbar_about:
                startActivity(new Intent(this, AboutTongue.class));
                break;
            case R.id.constest_actionbar_btn_submit:
                //此处有在activity中调用fragment的方法的演示
                int temp = consTestFrag.CheckAnswer();
                if (temp != 0) {
                    Toast.makeText(this, getString(R.string.constest_error_hint_1) + temp + getString(R.string.constest_error_hint_2),
                            Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, ConsTestResults.class));
                }
                break;
        }

    }

}