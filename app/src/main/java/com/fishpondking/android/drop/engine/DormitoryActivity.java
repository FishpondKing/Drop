package com.fishpondking.android.drop.engine;

import java.util.Date;

/**
 * Author: FishpondKing
 * Date: 2017/3/17:10:19
 * Email: song511653502@gmail.com
 * Description:
 */

public class DormitoryActivity {

    public static final int ACTIVITY_NOT_ENSURE = 0;
    public static final int ACTIVITY_ENSURE = 1;
    public static final int ACTIVITY_URGENCY = 2;
    public static final int ACTIVITY_FINISHED = 3;

    private String mDormitoryId;
    private int mStruts;
    private String mContent;
    private String mPersonId;
    private Date mActivityStartTime;
    private Date mActivityCreatedTime;

    public String getDormitoryId() {
        return mDormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        mDormitoryId = dormitoryId;
    }

    public int getStruts() {
        return mStruts;
    }

    public void setStruts(int struts) {
        mStruts = struts;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getPersonId() {
        return mPersonId;
    }

    public void setPersonId(String personId) {
        mPersonId = personId;
    }

    public Date getActivityStartTime() {
        return mActivityStartTime;
    }

    public void setActivityStartTime(Date activityStartTime) {
        mActivityStartTime = activityStartTime;
    }

    public Date getActivityCreatedTime() {
        return mActivityCreatedTime;
    }

    public void setActivityCreatedTime(Date activityCreatedTime) {
        mActivityCreatedTime = activityCreatedTime;
    }
}
