package com.meizu.mzroottools;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private View yb;
    private TextView unlockTitle, getMsgTitle;
    private LinearLayout linearLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    private float offset = 0;
    private int whichPosition = 0;

    private static final String TAG = "主程序";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        init();
    }

    @SuppressLint("ResourceType")
    private void init() {
        getMsgTitle = findViewById(R.id.getMsgTitle);
        unlockTitle = findViewById(R.id.unlockDevTitle);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("解锁工具");
        setSupportActionBar(toolbar);

        fragmentList.add(new GetDevMsgFragment());
        fragmentList.add(new UnLockDevFragment());
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MyPagerAdapter adapter = new MyPagerAdapter(fragmentManager);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        setPage(0);

        yb = findViewById(R.id.youbiao);
        linearLayout = findViewById(R.id.mytitle);
        linearLayout.setId(100);

        getMsgTitle.setOnClickListener(this);
        unlockTitle.setOnClickListener(this);

        offset = getWindowWidth() / 4 - yb.getLayoutParams().width / 2;
        setYbPosition((int) offset);

    }

    private int getWindowWidth() {
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        return width;
    }

    private void setYbPosition(int position) {
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(yb.getLayoutParams());
        params.leftMargin = position;
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(params);
        params1.addRule(RelativeLayout.BELOW, 100);
        yb.setLayoutParams(params1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (whichPosition == position) {
            float theOffset = positionOffset * getWindowWidth() / 2 + offset;
            setYbPosition((int) theOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getMsgTitle:
                setPage(0);
                break;
            case R.id.unlockDevTitle:
                setPage(1);
                break;
            default:
                break;
        }
    }

    public void setPage(int position){
        viewPager.setCurrentItem(position);
        if (position == 0) {
            getMsgTitle.setTextColor(Color.RED);
            unlockTitle.setTextColor(Color.BLACK);
        } else {
            getMsgTitle.setTextColor(Color.BLACK);
            unlockTitle.setTextColor(Color.RED);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}

