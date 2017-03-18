package com.fishpondking.android.drop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.Diary;
import com.fishpondking.android.drop.engine.DiaryLab;
import com.fishpondking.android.drop.utils.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: FishpondKing
 * Date: 2017/3/4:10:19
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryContentActivity extends BaseActivity {

    public static final String DIARY_POSITION = "diaryPosition";
    public static final int REFRESH_AUTHOR_HEAD = 11;

    private int mDiaryPosition;
    private DiaryLab mDiaryLab;
    private ArrayList<Diary> mDiaries;
    private Diary mDiary;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String mAuthorHeadUrl;
    private Handler mHandler;
    private Message mMessage;

    private Activity mActivity;
    private Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private ImageView mImageViewDiaryTopPhoto;
    private TextView mTextViewDiaryContentTitle;
    private CircleImageView mCircleImageViewDiaryAuthorHead;
    private TextView mTextViewDiaryContentAuthor;
    private TextView mTextViewDiaryContentDate;
    private TextView mTextViewDiaryContent;

    public static void activityStart(Context context, int diaryPosition){
        Intent intent = new Intent(context, DiaryContentActivity.class);
        intent.putExtra(DIARY_POSITION,diaryPosition);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mActivity = DiaryContentActivity.this;
        //获取传入的diaryPosition
        Intent intent = getIntent();
        mDiaryPosition = intent.getIntExtra(DIARY_POSITION,0);
        mDiaryLab = DiaryLab.get(mActivity);
        mDiaries = mDiaryLab.getDiaries();
        mDiary = mDiaries.get(mDiaryPosition);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case REFRESH_AUTHOR_HEAD:
                        updateAuthorHead();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        setContentView(R.layout.activity_diary_content);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_diary_content);

        //配置ToolBar
        //解决打开NestedScrollView时显示在中间的问题
        mToolbar.setFocusable(true);
        mToolbar.setFocusableInTouchMode(true);
        mToolbar.requestFocus();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollview_diary_content);
        mImageViewDiaryTopPhoto = (ImageView) findViewById(R.id.image_view_diary_content_photo);
        mTextViewDiaryContentTitle = (TextView) findViewById(R.id.text_view_diary_content_title);
        mCircleImageViewDiaryAuthorHead =
                (CircleImageView) findViewById(R.id.circle_image_view_diary_content_user_head);
        mTextViewDiaryContentAuthor = (TextView) findViewById(R.id.text_view_diary_content_author);
        mTextViewDiaryContentDate = (TextView) findViewById(R.id.text_view_diary_content_date);
        mTextViewDiaryContent = (TextView) findViewById(R.id.text_view_diary_content);
    }

    @Override
    protected void loadData() {

        //设置DiaryTopPhoto
        Glide.with(mActivity)
                .load(mDiary.getTopPhotoUrl())
                .into(mImageViewDiaryTopPhoto);

        //设置作者头像
        getAuthorHeadUrl();

        //设置日志内容
        mTextViewDiaryContentTitle.setText(mDiary.getTitle());
        mTextViewDiaryContentAuthor.setText(mDiary.getAuthor());
        mTextViewDiaryContentDate.setText(mDateFormat.format(mDiary.getDate()));
        mTextViewDiaryContent.setText(mDiary.getContent());

    }

    //获取作者头像url
    private void getAuthorHeadUrl(){
        String userheadPhotoName = "userHead" + mDiary.getAuthorId() + ".jpg";
        AVQuery<AVObject> userheadQuery = new AVQuery<AVObject>("_File");
        userheadQuery.whereEqualTo("name", userheadPhotoName);
        userheadQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null){
                    mAuthorHeadUrl = (String) avObject.get("url");
                    mMessage = new Message();
                    mMessage.what = REFRESH_AUTHOR_HEAD;
                    mHandler.sendMessage(mMessage);
                }
            }
        });
    }

    //设置作者头像
    private void updateAuthorHead(){

        SimpleTarget target = new SimpleTarget() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                mCircleImageViewDiaryAuthorHead.setImageBitmap((Bitmap) resource);
            }
        };

        Glide.with(mActivity)
                .load(mAuthorHeadUrl)
                .asBitmap()
                .into(target);
    }
}
