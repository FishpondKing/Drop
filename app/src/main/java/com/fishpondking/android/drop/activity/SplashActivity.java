package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVAnalytics;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.BaseActivity;
import com.fishpondking.android.drop.utils.SpUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/9:9:48
 * Email: song511653502@gmail.com
 * Description: 启动界面
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    //到达主页的延时
    private static final int TIME_TO_HOME = 2000;

    private boolean isSaveUserId;
    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;

    @Override
    protected void initVariables() {
        isSaveUserId = isSaveUserId(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //统计应用打开情况
        AVAnalytics.trackAppOpened(getIntent());

        //如果没有用户信息先进入功能引导页
        if (isSaveUserId) {
            //如果存储了用户信息，则显示启动屏
            setContentView(R.layout.activity_splash);
            //加载用户信息，宿舍信息
            mSingletonUser = SingletonUser.getInstance();
            mSingletonDormitory = SingletonDormitory.getInstance();
            SpUtils.loadUserState(this, mSingletonUser);
            SpUtils.loadDormitoryState(this, mSingletonDormitory);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    enterHomeActivity();
                }
            }, TIME_TO_HOME);

        } else {
            //如果没有存储用户信息，则进入引导页
            WelcomeGuideActivity.activityStart(this);
            finish();
            return;
        }

    }

    @Override
    protected void loadData() {

    }

    /**
     * Method: isFirstUse(Context context)
     * Description: 通过存储userId判断应用是否存储了用户信息
     * Param: context 传入当前的Activity
     * Return: 是否存储用户信息
     * Author: FishpondKing
     * Date: 2016/11/9:10:09
     */

    private boolean isSaveUserId(Context context) {
        String userId;
        userId = PreferenceManager.getDefaultSharedPreferences(context).getString("userId", "");
        if (userId == null || userId == "") {
            return false;
        } else return true;
    }

    private void enterHomeActivity() {
        HomeActivity.activityStart(this);
        finish();
    }
}
