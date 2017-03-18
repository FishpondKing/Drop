package com.fishpondking.android.drop.engine;

import java.util.Date;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:20:20
 * Email: song511653502@gmail.com
 * Description:
 */

public class Diary {

    private String mId;
    private String mTitle;
    private String mAuthorId;
    private String mAuthor;
    private String mDormitoryId;
    private String mContent;
    private Date mDate;
    private String mTopPhotoUrl;

    public Diary() {

    }

    public void clear(){
        mId = null;
        mTitle = null;
        mAuthor = null;
        mAuthorId = null;
        mDormitoryId = null;
        mContent = null;
        mDate = null;
        mTopPhotoUrl = null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTopPhotoUrl() {
        return mTopPhotoUrl;
    }

    public void setTopPhotoUrl(String topPhotoUrl) {
        mTopPhotoUrl = topPhotoUrl;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getDormitoryId() {
        return mDormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        mDormitoryId = dormitoryId;
    }
}
