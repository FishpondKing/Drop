package com.fishpondking.android.drop.listener;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.HomeActivity;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.SpUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/12/3:10:24
 * Email: song511653502@gmail.com
 * Description:
 */

public class CreateDormitoryListener implements View.OnClickListener {

    private Activity mActivity;
    private TextInputEditText mTextInputEditText;
    private Button mButton;

    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private DecimalFormat df = new DecimalFormat("000000");
    private int mOldDormitoryId;
    private String mDormitoryId;
    private String mDormitoryName;
    private ArrayList<String> mDormitoryMembers;
    private Thread mThread;

    public CreateDormitoryListener(Activity activity, Button button,
                                   TextInputEditText dormitoryName) {

        mActivity = activity;
        mButton = button;
        mTextInputEditText = dormitoryName;

        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();

        mThread = Thread.currentThread();
    }

    @Override
    public void onClick(View v) {

        mDormitoryName = mTextInputEditText.getText().toString();

        //获取寝室Id
        AVQuery<AVObject> query = new AVQuery<>("UtilData");
        query.whereEqualTo("scope", "all");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    mOldDormitoryId = avObject.getInt("dormitoryCount");
                    mOldDormitoryId++;
                    avObject.put("dormitoryCount", mOldDormitoryId);
                    avObject.saveInBackground();
                    mDormitoryId = df.format(mOldDormitoryId);

                    //初始化Dormitory的成员列表
                    mDormitoryMembers = new ArrayList<String>();
                    mDormitoryMembers.add(mSingletonUser.getId());

                    //向数据库提交Dormitory数据
                    AVObject dormitory = new AVObject("Dormitory");
                    dormitory.put("dormitoryId", mDormitoryId);
                    dormitory.put("dormitoryName", mDormitoryName);
                    dormitory.put("dormitoryLeader", mSingletonUser.getId());
                    dormitory.put("dormitoryMembers", mDormitoryMembers);
                    dormitory.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 存储成功
                                Toast.makeText(mActivity, mActivity.getResources()
                                                .getString(R.string.create_dormitory_success),
                                        Toast.LENGTH_SHORT)
                                        .show();

                                //保存SingletonDormitory状态信息
                                mSingletonDormitory.setId(mDormitoryId);
                                mSingletonDormitory.setName(mDormitoryName);
                                mSingletonDormitory.setLeader(mSingletonUser.getId());
                                mSingletonDormitory.setMembers(mDormitoryMembers);

                                //向数据库更新User的数据
                                mSingletonUser.setLeader(true);
                                mSingletonUser.put("isLeader", true);
                                mSingletonUser.setDormitoryId(mDormitoryId);
                                mSingletonUser.put("dormitoryId", mDormitoryId);
                                mSingletonUser.saveInBackground();
                                SpUtils.saveUserState(mActivity, mSingletonUser);
                                HomeActivity.activityStart(mActivity);
                                mActivity.finish();
                                return;
                            } else {
                                // 失败，请检查网络环境以及 SDK 配置是否正确
                                Toast.makeText(mActivity, R.string.create_dormitory_fail,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                } else {
                    //创建失败，请检查网络环境是否正常
                    Toast.makeText(mActivity, mActivity.getResources()
                            .getString(R.string.create_dormitory_fail), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
        });
    }
}
