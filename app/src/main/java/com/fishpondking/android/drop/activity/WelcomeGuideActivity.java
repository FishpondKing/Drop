package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.adapter.WelcomeGuidePagerAdapter;
import com.fishpondking.android.drop.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/9:10:13
 * Email: song511653502@gmail.com
 * Description: 引导页
 */

public class WelcomeGuideActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = WelcomeGuideActivity.class.getSimpleName();
    //引导页图片资源
    private static final int[] PICS = {R.layout.image_view_welcome_guide_1,
            R.layout.image_view_welcome_guide_2,
            R.layout.image_view_welcome_guide_3,
            R.layout.image_view_welcome_guide_4
    };

    private ViewPager mViewPager;
    private WelcomeGuidePagerAdapter mWelcomeGuidePagerAdapter;
    private Button mSigninButton;
    private Button mLoginButton;
    private List<View> mViews;
    //底部小圆点图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, WelcomeGuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome_guide);


        mViewPager = (ViewPager) findViewById(R.id.view_pager_welcome_guide);
        mViews = new ArrayList<View>();
        for (int i = 0; i < PICS.length; i++) {
            View view = LayoutInflater.from(this).inflate(PICS[i], null);
            mViews.add(view);
        }
        mWelcomeGuidePagerAdapter = new WelcomeGuidePagerAdapter(mViews);
        mViewPager.setAdapter(mWelcomeGuidePagerAdapter);
        mViewPager.addOnPageChangeListener(new PageChangeListener());

        initDots();

        mSigninButton = (Button) findViewById(R.id.button_welcome_guide_signin);
        mSigninButton.setOnClickListener(this);

        mLoginButton = (Button) findViewById(R.id.button_welcome_guide_login);
        mLoginButton.setOnClickListener(this);

    }

    @Override
    protected void loadData() {

    }

    private void initDots() {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_welcome_dot);
        dots = new ImageView[mWelcomeGuidePagerAdapter.getCount()];

        //循环取得小圆点
        for (int i = 0; i < dots.length; i++) {
            dots[i] = (ImageView) mLinearLayout.getChildAt(i);
            dots[i].setEnabled(false);
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true);
    }

    /**
     * Method: setCurrentDot(int position)
     * Description: 设置小圆点的位置为当前引导图片位置
     * Param: position 当前引导页图片位置
     * Return: void
     * Author: FishpondKing
     * Date: 2016/11/10:18:19
     */

    public void setCurrentDot(int position) {
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setCurrentDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_welcome_guide_login:
                LoginAndSigninActivity.activityStart(this, 0);
            case R.id.button_welcome_guide_signin:
                LoginAndSigninActivity.activityStart(this, 1);
            default:
        }
    }
}
