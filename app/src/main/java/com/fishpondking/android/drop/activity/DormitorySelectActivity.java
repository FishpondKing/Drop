package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.utils.BaseActivity;

/**
 * Author: FishpondKing
 * Date: 2016/11/25:9:29
 * Email: song511653502@gmail.com
 * Description:
 */

public class DormitorySelectActivity extends BaseActivity {

    private TextInputEditText mTiEditTextDormitoryId;
    private Button mButtonJoinDormitory;
    private TextInputEditText mTiDormitoryName;
    private Button mButtonCreateDormitory;


    public static void activityStart(Context context){
        Intent intent = new Intent(context, DormitorySelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        setContentView(R.layout.activity_dormitory_select);

        mButtonJoinDormitory = (Button) findViewById(R.id.button_dormitory_select_join);

        mButtonCreateDormitory = (Button) findViewById(R.id.button_dormitory_select_create);

    }

    @Override
    protected void loadData() {

    }
}
