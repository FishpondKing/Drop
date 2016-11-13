package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVAnalytics;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.utils.BaseActivity;

/**
 * Author: FishpondKing
 * Date: 2016/11/9:9:48
 * Email: song511653502@gmail.com
 * Description: 启动界面
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    //是否是第一次启动
    private static final String IS_FIRST_USE = "isFirstUse";
    //到达主页的延时
    private static final int TIME_TO_HOME = 2000;

    private boolean isFirstUse;

    @Override
    protected void initVariables() {
        isFirstUse = isFirstUse(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //统计应用打开情况
        AVAnalytics.trackAppOpened(getIntent());

        //如果是第一次启动先进入功能引导页，并将IS_FIRST_USE设为false
        if (isFirstUse) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(IS_FIRST_USE, false)
                    .apply();
            WelcomeGuideActivity.activityStart(this);
            finish();
            return;
        } else {
            //如果不是第一次启动，则正常显示启动屏
            setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    enterHomeActivity();
                }
            }, TIME_TO_HOME);
        }

    }

    @Override
    protected void loadData() {

    }

    /**
     * Method: isFirstUse(Context context)
     * Description: 判断应用是否为第一次启动
     * Param: context 传入当前的Activity
     * Return: 是否为第一次启动
     * Author: FishpondKing
     * Date: 2016/11/9:10:09
     */

    private boolean isFirstUse(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_FIRST_USE, true);
    }

    private void enterHomeActivity() {
        HomeActivity.activityStart(this);
        finish();
    }
}
