package com.fishpondking.android.drop.listener;

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

import java.util.ArrayList;

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

    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private String mDormitoryId;
    private String mDormitoryName;
    private ArrayList<String> mDormitoryMembers;

    public JoinDormitoryListener(Context context, Button button, TextInputEditText dormitoryId){

        mContext = context;
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
                if(e == null){
                    //查询寝室成功
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.join_dormitory_success), Toast.LENGTH_SHORT)
                            .show();
                    mDormitoryMembers = (ArrayList) avObject.getList("dormitoryMembers");
                    mDormitoryMembers.add(mSingletonUser.getId());
                    avObject.put("dormitoryMembers", mDormitoryMembers);
                    avObject.saveInBackground();
                    mSingletonUser.setLeader(false);
                    mSingletonUser.put("isLeader",false);
                    mSingletonUser.setDormitoryId(avObject.getString("dormitoryId"));
                    mSingletonUser.put("dormitoryId", avObject.getString("dormitoryId"));
                    mSingletonUser.saveInBackground();
                    HomeActivity.activityStart(mContext);
                }else {
                    //查询寝室失败
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.join_dormitory_fail), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }
}
