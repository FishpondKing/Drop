package com.fishpondking.android.drop.engine;

import java.util.Date;

/**
 * Author: FishpondKing
 * Date: 2017/3/15:20:06
 * Email: song511653502@gmail.com
 * Description:
 */

public class MoneyEvent {

    public static final int MONEY_OUT_EVENT = 0;
    public static final int MONEY_IN_EVENT = 1;

    private String mDormitoryId;
    private int mEventType;
    private String mReason;
    private double mCount;
    private Date mDate;

    public String getDormitoryId() {
        return mDormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        mDormitoryId = dormitoryId;
    }

    public int getEventType() {
        return mEventType;
    }

    public void setEventType(int eventType) {
        mEventType = eventType;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public double getCount() {
        return mCount;
    }

    public void setCount(double count) {
        mCount = count;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
