package net.chinancd.consgenius.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import net.chinancd.consgenius.R;

import java.util.ArrayList;

/**
 * Created by hedefu
 * on 2016 0411 at 15:57 .
 * email:hedefu999@gmail.com
 */
public class Welcome extends Activity {
    private Button button;

    private ViewPager viewPager;
    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    SharedPreferences sharedPreferences;
    private int actionnum;
    ArrayList<View> views;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        button = (Button) findViewById(R.id.welcome_bn);
        sharedPreferences = getSharedPreferences("whatsnew", 0);
        //下述的get方法会改变数值，只能使用num才可以不改变,num充当代言人的功能，get方法反而会改变数值。。。。
        actionnum = sharedPreferences.getInt("num", 1);
        //editor = sharedPreferences.edit();
        if (actionnum != 0) {//非0表示要求展现what'new界面
            sharedPreferences.edit().putInt("num", 0).commit();
            setContentView(R.layout.welcome_main);
        } else {//0表示不启动whatsnew界面
            setContentView(R.layout.welcome_main);
            startActivity(new Intent(Welcome.this, Mainactivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);

        //此处find操作不可以移到前面
        viewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imageView0.setImageResource(R.drawable.welcome_pagenow);
                        imageView1.setImageResource(R.drawable.welcome_page);
                        break;
                    case 1:
                        imageView0.setImageResource(R.drawable.welcome_page);
                        imageView1.setImageResource(R.drawable.welcome_pagenow);
                        imageView2.setImageResource(R.drawable.welcome_page);
                        break;
                    case 2:
                        imageView1.setImageResource(R.drawable.welcome_page);
                        imageView2.setImageResource(R.drawable.welcome_pagenow);
                        imageView3.setImageResource(R.drawable.welcome_page);
                        break;
                    case 3:
                        imageView2.setImageResource(R.drawable.welcome_page);
                        imageView3.setImageResource(R.drawable.welcome_pagenow);
                        break;
                }
            }

            public void onPageScrollStateChanged(int state) {

            }
        });
        //下述find代码不能放到前面，否则会在上面的case1下的第一行报错，原因不明
        imageView0 = (ImageView) findViewById(R.id.welcome_page0);
        imageView1 = (ImageView) findViewById(R.id.welcome_page1);
        imageView2 = (ImageView) findViewById(R.id.welcome_page2);
        imageView3 = (ImageView) findViewById(R.id.welcome_page3);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view0 = layoutInflater.inflate(R.layout.welcome_page0, null);
        View view1 = layoutInflater.inflate(R.layout.welcome_page1, null);
        View view2 = layoutInflater.inflate(R.layout.welcome_page2, null);
        View view3 = layoutInflater.inflate(R.layout.welcome_page3, null);
        views = new ArrayList<>();
        views.add(view0);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                (container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                (container).removeView(views.get(position));
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    public void itemOnClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_bn:
                if (actionnum == 2) {//2表示应用内请求whatsnew界面
                    sharedPreferences.edit().putInt("num", 0).commit();
                    finish();
                } else {
                    startActivity(new Intent(Welcome.this, Mainactivity.class));
                    finish();
                }

                break;
            case R.id.welcome_page0:
                imageView0.setImageResource(R.drawable.welcome_pagenow);
                break;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }
}