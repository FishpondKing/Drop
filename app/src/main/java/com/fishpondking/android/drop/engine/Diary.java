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
    private String mAuthor;
    private String mContent;
    private Date mDate;
    private String mPhoto;

    public Diary() {

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

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }
}
