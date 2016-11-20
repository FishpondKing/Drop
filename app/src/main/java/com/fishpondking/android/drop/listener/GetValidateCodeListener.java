package com.fishpondking.android.drop.listener;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.RegexUtils;
import com.fishpondking.android.drop.utils.SendSmsTimerUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:16:57
 * Email: song511653502@gmail.com
 * Description:
 */

public class GetValidateCodeListener implements View.OnClickListener {

    private Button mButton;
    private String mUserTel;
    private String mUserName;
    private String mUserPassword;
    private String mUserPasswordCheck;
    private SingletonUser mSingletonUser;
    private Context mContext;
    private SendSmsTimerUtils mSendSmsTimerUtils;


    public GetValidateCodeListener(Context context, Button button,String userTel,String userName,
                                   String userPassword, String userPasswordCheck){
        mContext = context;
        mButton = button;
        mUserTel = "13399801969";
        mUserName = "song";
        mUserPassword = "123456";
        mUserPasswordCheck = "123456";
        mSingletonUser = SingletonUser.getInstance();
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(mContext,mUserTel,Toast.LENGTH_SHORT).show();

        //检查电话号码格式
        if(RegexUtils.isMobileExact(mUserTel) == false){
            Toast.makeText(mContext,mContext.getResources()
                    .getString(R.string.user_tel_wrong),Toast.LENGTH_SHORT).show();
            return;
        }

        //检查用户名格式
        if(RegexUtils.isUsername(mUserName) == false){
            Toast.makeText(mContext,mContext.getResources()
                    .getString(R.string.user_name_wrong),Toast.LENGTH_SHORT).show();
            return;
        }

        //检查用户密码格式
        if(RegexUtils.isUserpassword(mUserPassword) == false){
            Toast.makeText(mContext,mContext.getResources()
                    .getString(R.string.user_password_wrong),Toast.LENGTH_SHORT).show();
            return;
        }

        //检查用户确认密码与密码是否一致
        if(!mUserPasswordCheck.equals(mUserPassword)){
            Toast.makeText(mContext,mContext.getResources()
                    .getString(R.string.user_password_check_wrong),Toast.LENGTH_SHORT).show();
            return;
        }

        //发送验证码,上传用户名和手机号都设置为用户手机号
        mSingletonUser.setMobilePhoneNumber(mUserTel);
        mSingletonUser.setUsername(mUserTel);
        mSingletonUser.put("userNickName", mUserName);
        mSingletonUser.setPassword(mUserPassword);

        mSendSmsTimerUtils = new SendSmsTimerUtils(mButton, 20000, 1000,R.color.blue500,R.color.grey500);
        mSingletonUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // successfully
                    Toast.makeText(mContext,mContext.getResources()
                            .getString(R.string.get_validate_code_success),Toast.LENGTH_SHORT)
                            .show();
                    //处理验证码按钮等待20s
                    mSendSmsTimerUtils.start();
                } else {
                    // failed
                    Toast.makeText(mContext,mContext.getResources()
                            .getString(R.string.get_validate_code_fail),Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
