package net.chinancd.consgenius.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import net.chinancd.consgenius.R;

/**
 * Created by hedefu
 * on 2016 0411 at 15:55 .
 * email:hedefu999@gmail.com
 */
public class Splash extends Activity {
    private int splashTime = 4;
    private Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        super.onCreate(savedInstanceState);
        //两种延时退出方式
        //skipActivity(3);
        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500 * splashTime);
                    }
                } catch (InterruptedException e) {
                } finally {
                    startActivity(new Intent(Splash.this, Welcome.class));
                    finish();
                }
                super.run();
            }
        };
        splashThread.start();
    }


    //等不及的话，触摸屏幕就可以退出splash
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashThread) {
                splashThread.notifyAll();
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return false;
        }
        return false;
    }
}