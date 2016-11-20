package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.adapter.LoginAndSigninAdapter;
import com.fishpondking.android.drop.utils.BaseActivity;

/**
 * Author: FishpondKing
 * Date: 2016/11/11:15:33
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginAndSigninActivity extends BaseActivity {

    private static final String TAG = LoginAndSigninActivity.class.getSimpleName();
    //登录界面则为0，注册界面为1
    private static final String EXTRA_LOGIN_OR_SIGNIN =
            "com.fishpondking.android.drop.login_or_signin";

    private TabLayout mTabLayout;
    private TabLayout.Tab mTab;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;

    private int mLoginOrSignin;

    public static void activityStart(Context context, int loginOrSignin) {
        Intent intent = new Intent(context, LoginAndSigninActivity.class);
        intent.putExtra(EXTRA_LOGIN_OR_SIGNIN, loginOrSignin);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        //接受WelcomeGuideActivity传过来选择界面的extra，登录界面为0，注册界面为1，默认为0
        mLoginOrSignin = getIntent().getIntExtra(EXTRA_LOGIN_OR_SIGNIN, 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loginandsignin);

        mFragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.view_pager_login_and_singin);
        mViewPager.setAdapter(new LoginAndSigninAdapter(this, mFragmentManager));

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_login_and_singin);
        mTabLayout.setupWithViewPager(mViewPager);
        mTab = mTabLayout.getTabAt(mLoginOrSignin);
        mTab.select();

    }

    @Override
    protected void loadData() {

    }
}
