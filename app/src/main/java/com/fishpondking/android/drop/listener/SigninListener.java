package com.fishpondking.android.drop.listener;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.DormitorySelectActivity;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.RegexUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/19:15:05
 * Email: song511653502@gmail.com
 * Description:
 */

public class SigninListener implements View.OnClickListener {

    private Activity mActivity;
    private TextInputEditText mTiEditTextValidateCode;
    private Button mButton;

    private SingletonUser mSingletonUser;
    private String mValidateCode;

    public SigninListener(Activity activity, Button button, TextInputEditText validateCode) {

        mActivity = activity;
        mTiEditTextValidateCode = validateCode;
        mButton = button;

        mSingletonUser = SingletonUser.getInstance();

    }

    @Override
    public void onClick(View v) {

        //获取验证码
        mValidateCode = mTiEditTextValidateCode.getText().toString();

        //检查验证码格式
        if (RegexUtils.isValidatecode(mValidateCode) == false) {
            Toast.makeText(mActivity, mActivity.getResources()
                    .getString(R.string.user_validate_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        mSingletonUser.verifyMobilePhoneInBackground(mValidateCode,
                new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 验证成功
                            Toast.makeText(mActivity, mActivity.getResources()
                                    .getString(R.string.signin_success), Toast.LENGTH_SHORT).show();
                            DormitorySelectActivity.activityStart(mActivity);
                            mActivity.finish();
                            return;
                        } else {
                            // 验证失败
                            Toast.makeText(mActivity, mActivity.getResources()
                                    .getString(R.string.signin_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
