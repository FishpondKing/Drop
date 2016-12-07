package com.fishpondking.android.drop.listener;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.HomeActivity;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.SpUtils;

import java.util.ArrayList;

/**
 * Author: FishpondKing
 * Date: 2016/12/3:10:23
 * Email: song511653502@gmail.com
 * Description:
 */

public class JoinDormitoryListener implements View.OnClickListener {

    private Activity mActivity;
    private TextInputEditText mTextInputEditText;
    private Button mButton;

    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private String mDormitoryId;
    private ArrayList<String> mDormitoryMembers;

    public JoinDormitoryListener(Activity activity, Button button, TextInputEditText dormitoryId) {

        mActivity = activity;
        mTextInputEditText = dormitoryId;
        mButton = button;

        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();
    }

    @Override
    public void onClick(View v) {

        mDormitoryId = mTextInputEditText.getText().toString();

        //根据寝室ID查询寝室
        AVQuery<AVObject> query = new AVQuery<>("Dormitory");
        query.whereEqualTo("dormitoryId", mDormitoryId);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    //查询寝室成功
                    Toast.makeText(mActivity, mActivity.getResources()
                            .getString(R.string.join_dormitory_success), Toast.LENGTH_SHORT)
                            .show();
                    //存储SingletonDormitory信息
                    mSingletonDormitory.setId(mDormitoryId);
                    mSingletonDormitory.setName(avObject.getString("dormitoryName"));
                    mSingletonDormitory.setLeader(avObject.getString("dormitoryLeader"));
                    mDormitoryMembers = (ArrayList) avObject.getList("dormitoryMembers");
                    mDormitoryMembers.add(mSingletonUser.getId());
                    mSingletonDormitory.setMembers(mDormitoryMembers);
                    avObject.put("dormitoryMembers", mDormitoryMembers);
                    avObject.saveInBackground();
                    //存储SingletonUser信息
                    mSingletonUser.setLeader(false);
                    mSingletonUser.put("isLeader", false);
                    mSingletonUser.setDormitoryId(avObject.getString("dormitoryId"));
                    mSingletonUser.put("dormitoryId", avObject.getString("dormitoryId"));
                    mSingletonUser.saveInBackground();
                    //保存登录状态
                    SpUtils.saveUserState(mActivity, mSingletonUser);
                    SpUtils.saveDormitoryState(mActivity, mSingletonDormitory);
                    //页面跳转
                    HomeActivity.activityStart(mActivity);
                    mActivity.finish();
                    return;
                } else {
                    //查询寝室失败
                    Toast.makeText(mActivity, mActivity.getResources()
                            .getString(R.string.join_dormitory_fail), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
}
