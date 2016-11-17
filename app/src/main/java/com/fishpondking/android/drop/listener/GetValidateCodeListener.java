package com.fishpondking.android.drop.listener;

import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.RegexUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:16:57
 * Email: song511653502@gmail.com
 * Description:
 */

public class GetValidateCodeListener implements View.OnClickListener {

    private String mUserTel;
    private SingletonUser mSingletonUser;

    public GetValidateCodeListener(String userTel){
        Log.d("SMS","get");
        mUserTel = userTel;
    }

    @Override
    public void onClick(View v) {
        Log.d("SMS","验证码");
        if(RegexUtils.isMobileExact(mUserTel) == false){
            return;
        }
        mSingletonUser = SingletonUser.getInstance();
        mSingletonUser.setMobilePhoneNumber(mUserTel);
        //发送验证码
        mSingletonUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // successfully
                    Log.d("SMS","验证码获取成功");
                } else {
                    // failed
                    Log.d("SMS","验证码获取失败");
                }
            }
        });
    }
}
