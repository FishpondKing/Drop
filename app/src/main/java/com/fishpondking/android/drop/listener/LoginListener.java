package com.fishpondking.android.drop.listener;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.DormitorySelectActivity;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.RegexUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/19:16:11
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginListener implements View.OnClickListener{

    private Context mContext;
    private TextInputEditText mTiEditTextUserTel;
    private TextInputEditText mTiEditTextUserPassword;
    private Button mButton;

    private SingletonUser mSingletonUser;
    private String mUserTel;
    private String mUserPassword;

    public LoginListener(Context context, Button button, TextInputEditText userTel,
                         TextInputEditText userPassword){

        mContext = context;
        mButton = button;
        mTiEditTextUserTel = userTel;
        mTiEditTextUserPassword = userPassword;

        mSingletonUser = SingletonUser.getInstance();
    }

    @Override
    public void onClick(View v) {

        //获取手机号，密码
        mUserTel = mTiEditTextUserTel.getText().toString();
        mUserPassword = mTiEditTextUserPassword.getText().toString();

        //检查手机号码的格式
        if (RegexUtils.isMobileExact(mUserTel) == false) {
            Toast.makeText(mContext, mContext.getResources()
                    .getString(R.string.user_tel_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        //使用手机号和密码登录
        mSingletonUser.loginByMobilePhoneNumberInBackground(mUserTel, mUserPassword,
                new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    // 登录成功
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                } else {
                    // 登录失败
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
