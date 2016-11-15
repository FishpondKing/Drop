package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.utils.BaseActivity;

/**
 * Author: FishpondKing
 * Date: 2016/11/11:15:33
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginAndSigninActivity extends BaseActivity{

    private static final String TAG = LoginAndSigninActivity.class.getSimpleName();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, LoginAndSigninActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loginandsignin);

        mFragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager)findViewById(R.id.view_pager_login_and_singin);
        mViewPager.setAdapter();

    }

    @Override
    protected void loadData() {

    }
}
