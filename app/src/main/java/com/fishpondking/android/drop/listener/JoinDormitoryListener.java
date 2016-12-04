package com.fishpondking.android.drop.listener;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.fishpondking.android.drop.engine.SingletonDormitory;

/**
 * Author: FishpondKing
 * Date: 2016/12/3:10:23
 * Email: song511653502@gmail.com
 * Description:
 */

public class JoinDormitoryListener implements View.OnClickListener {

    private Context mContext;
    private TextInputEditText mTextInputEditText;
    private Button mButton;

    private SingletonDormitory mSingletonDormitory;

    public JoinDormitoryListener(Context context, Button button, TextInputEditText dormitoryId){

        mContext = context;
        mTextInputEditText = dormitoryId;
        mButton = button;

        mSingletonDormitory = SingletonDormitory.getInstance();
    }

    @Override
    public void onClick(View v) {

    }
}
