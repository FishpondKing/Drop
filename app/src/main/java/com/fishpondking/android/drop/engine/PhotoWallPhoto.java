package com.fishpondking.android.drop.engine;

import java.util.Date;

/**
 * Author: FishpondKing
 * Date: 2017/3/5:14:53
 * Email: song511653502@gmail.com
 * Description:
 */

public class PhotoWallPhoto {

    private String mId;
    private String mName;
    private String mAuthorId;
    private String mAuthor;
    private String mDormitoryId;
    private String mDescription;
    private Date mDate;
    private String mPhotoUrl;

    public PhotoWallPhoto(){

    }

    public void clear(){
        mId = null;
        mName = null;
        mAuthorId = null;
        mAuthor = null;
        mDormitoryId = null;
        mDescription = null;
        mDate = null;
        mPhotoUrl = null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDormitoryId() {
        return mDormitoryId;
    }

    public void setDormitoryId(String dormitoryId) {
        mDormitoryId = dormitoryId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

}
