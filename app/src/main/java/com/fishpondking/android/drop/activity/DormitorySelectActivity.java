package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.listener.CreateDormitoryListener;
import com.fishpondking.android.drop.listener.JoinDormitoryListener;
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
    private TextInputEditText mTiEditTextDormitoryName;
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

        mTiEditTextDormitoryId = (TextInputEditText)
                findViewById(R.id.ti_edit_text_dormitory_select_id);

        mButtonJoinDormitory = (Button) findViewById(R.id.button_dormitory_select_join);
        mButtonJoinDormitory.setOnClickListener(new JoinDormitoryListener(this,
                mButtonJoinDormitory, mTiEditTextDormitoryId));

        mTiEditTextDormitoryName = (TextInputEditText)
                findViewById(R.id.ti_edit_text_dormitory_select_name);

        mButtonCreateDormitory = (Button) findViewById(R.id.button_dormitory_select_create);
        mButtonCreateDormitory.setOnClickListener(new CreateDormitoryListener(this,
                mButtonCreateDormitory, mTiEditTextDormitoryName));


    }

    @Override
    protected void loadData() {

    }
}
