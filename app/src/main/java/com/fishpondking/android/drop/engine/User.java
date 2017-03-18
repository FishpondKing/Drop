package com.fishpondking.android.drop.engine;

import com.avos.avoscloud.AVUser;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:9:49
 * Email: song511653502@gmail.com
 * Description:
 */

public class User extends AVUser {

    private String mId;
    private String mUserTel;
    private String mUserName;
    private String mUserPassword;
    private String mDormitoryId;
    private boolean isLeader;
    private String mUserHeadPhotoUrl;

    public User(){
        mId = null;
        mUserTel = null;
        mUserName = null;
        mUserPassword = null;
        mDormitoryId = null;
        isLeader = false;
        mUserHeadPhotoUrl = null;
    }

    public void clear(){
        mId = null;
        mUserTel = null;
        mUserName = null;
        mUserPassword = null;
        mDormitoryId = null;
        isLeader = false;
        mUserHeadPhotoUrl = null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserTel() {
        return mUserTel;
    }

    public void setUserTel(String userTel) {
        mUserTel = userTel;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }

    public String getDormitoryId() {
        return mDormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        mDormitoryId = dormitoryId;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public String getUserHeadPhotoUrl() {
        return mUserHeadPhotoUrl;
    }

    public void setUserHeadPhotoUrl(String userHeaderUrl) {
        mUserHeadPhotoUrl = userHeaderUrl;
    }
}
