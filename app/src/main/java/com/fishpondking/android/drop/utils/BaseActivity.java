package com.fishpondking.android.drop.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avos.avoscloud.AVException;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:10:51
 * Email: song511653502@gmail.com
 * Description:
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);

        initVariables();
        try {
            initViews(savedInstanceState);
        } catch (AVException e) {
            e.printStackTrace();
        }
        loadData();

    }

    /**
     * Method: initVariables()
     * Description: 初始化变量，包括Intent带的数据和Activity内的变量
     * Param:
     * Return: void
     * Author: FishpondKing
     * Date: 2016/11/3:15:24
     */

    protected abstract void initVariables();

    /**
     * Method: initViews(Bundle savedInstanceState)
     * Description: 加载layout布局文件，初始化控件，为控件挂上事件方法
     * Param: savedInstanceState 同onCreate方法
     * Return: void
     * Author: FishpondKing
     * Date: 2016/11/3:15:25
     */

    protected abstract void initViews(Bundle savedInstanceState) throws AVException;
    
    /**
     * Method: loadData();
     * Description: 调用MobileAPI获取数据
     * Param: 
     * Return: void
     * Author: FishpondKing
     * Date: 2016/11/3:15:30
     */
    
    protected abstract void loadData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
