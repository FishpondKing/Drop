package com.fishpondking.android.drop.listener;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.DormitorySelectActivity;
import com.fishpondking.android.drop.activity.HomeActivity;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.RegexUtils;
import com.fishpondking.android.drop.utils.SpUtils;

import java.util.ArrayList;

/**
 * Author: FishpondKing
 * Date: 2016/11/19:16:11
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginListener implements View.OnClickListener {

    private Activity mActivity;
    private TextInputEditText mTiEditTextUserTel;
    private TextInputEditText mTiEditTextUserPassword;
    private Button mButton;

    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private String mUserTel;
    private String mUserPassword;
    private String mUserDormitoryId;
    private Boolean mUserIsLeader;

    public LoginListener(Activity activity, Button button, TextInputEditText userTel,
                         TextInputEditText userPassword) {

        mActivity = activity;
        mButton = button;
        mTiEditTextUserTel = userTel;
        mTiEditTextUserPassword = userPassword;

        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();
    }

    @Override
    public void onClick(View v) {

        //获取手机号，密码
        mUserTel = mTiEditTextUserTel.getText().toString();
        mUserPassword = mTiEditTextUserPassword.getText().toString();

        //检查手机号码的格式
        if (RegexUtils.isMobileExact(mUserTel) == false) {
            Toast.makeText(mActivity, mActivity.getResources()
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
                            mSingletonUser.setId(avUser.getObjectId());
                            mSingletonUser.setUserTel(avUser.getMobilePhoneNumber());
                            mSingletonUser.setUserName((String) avUser.get("userNickName"));

                            mUserDormitoryId = avUser.getString("dormitoryId");

                            //判断用户是否已经加入寝室
                            if (mUserDormitoryId == null || mUserDormitoryId == "") {
                                Toast.makeText(mActivity, mActivity.getResources()
                                        .getString(R.string.login_success), Toast.LENGTH_SHORT)
                                        .show();
                                DormitorySelectActivity.activityStart(mActivity);
                                mActivity.finish();
                                return;
                            } else {
                                Toast.makeText(mActivity, mActivity.getResources()
                                        .getString(R.string.login_success), Toast.LENGTH_SHORT)
                                        .show();
                                mUserIsLeader = avUser.getBoolean("isLeader");

                                mSingletonUser.setLeader(mUserIsLeader);
                                mSingletonUser.setDormitoryId(mUserDormitoryId);

                                AVQuery<AVObject> query = new AVQuery<>("Dormitory");
                                query.whereEqualTo("dormitoryId", mUserDormitoryId);
                                query.getFirstInBackground(new GetCallback<AVObject>() {
                                    @Override
                                    public void done(AVObject avObject, AVException e) {
                                        //存储SingletonDormitory信息
                                        mSingletonDormitory
                                                .setId(avObject.getString("dormitoryId"));
                                        mSingletonDormitory
                                                .setName(avObject.getString("dormitoryName"));
                                        mSingletonDormitory
                                                .setLeader(avObject.getString("dormitoryLeader"));
                                        mSingletonDormitory
                                                .setMembers((ArrayList)avObject
                                                        .getList("dormitoryMembers"));
                                    }
                                });
                                //保存用户登录状态信息
                                SpUtils.saveUserState(mActivity,mSingletonUser);
                                SpUtils.saveDormitoryState(mActivity, mSingletonDormitory);

                                HomeActivity.activityStart(mActivity);
                                mActivity.finish();
                                return;
                            }


                        } else {
                            // 登录失败
                            Toast.makeText(mActivity, mActivity.getResources()
                                    .getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
