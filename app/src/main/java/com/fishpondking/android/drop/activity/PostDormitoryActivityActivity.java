package com.fishpondking.android.drop.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.DormitoryActivity;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.fragment.DormitoryActivityFragment;
import com.fishpondking.android.drop.utils.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Author: FishpondKing
 * Date: 2017/3/17:8:30
 * Email: song511653502@gmail.com
 * Description:
 */

public class PostDormitoryActivityActivity extends BaseActivity {

    public static final int REFRESH_MEMBER_LIST = 41;
    public static final int LOAD_CHECK_BOX_FINISH = 42;

    private Activity mActivity;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private EditText mEditTextActivityContent;
    private TextView mTextViewDate;
    private ImageButton mImageButtonDatePicker;
    private TextView mTextViewTime;
    private ImageButton mImageButtonTimePicker;
    private LinearLayout mLinearLayoutCheckBox;

    private SingletonDormitory mSingletonDormitory;
    private DormitoryActivity mDormitoryActivity;
    private Handler mHandler;
    private Message mMessage;
    private ArrayList<String> mDormitoryMemberList;
    private ArrayList<CheckBox> mCheckBoxes;
    private int mLoadCheckCount;
    private int mLoadFinishedCheckBoxCount;

    public static void activityStart(Context context, Fragment fragment) {
        Intent intent = new Intent(context, PostDormitoryActivityActivity.class);
        fragment.startActivityForResult(intent, DormitoryActivityFragment.REQUEST_ADD_DORMITORY_ACTIVITY);
    }

    @Override
    protected void initVariables() {
        mActivity = PostDormitoryActivityActivity.this;
        mSingletonDormitory = SingletonDormitory.getInstance();
        mDormitoryActivity = new DormitoryActivity();
        mCheckBoxes = new ArrayList<CheckBox>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_MEMBER_LIST:
                        mDormitoryMemberList = mSingletonDormitory.getMembers();
                        mLoadCheckCount = mDormitoryMemberList.size();
                        mLoadFinishedCheckBoxCount = 0;
                        setPersonCheckBoxList();
                        break;
                    case LOAD_CHECK_BOX_FINISH:
                        setContentView(mCoordinatorLayout);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        mCoordinatorLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_post_dormitory_activity, null);
        setContentView(mCoordinatorLayout);

        //配置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_post_dormitory_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextActivityContent = (EditText) findViewById(R.id.edit_text_post_activity_activity_content);

        mTextViewDate = (TextView) findViewById(R.id.text_view_post_activity_activity_date);
        mImageButtonDatePicker = (ImageButton) findViewById(R.id.image_button_post_activity_date_picker);
        mImageButtonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDormitoryActivityDate();
            }
        });

        mTextViewTime = (TextView) findViewById(R.id.text_view_post_activity_activity_time);
        mImageButtonTimePicker = (ImageButton) findViewById(R.id.image_button_post_activity_time_picker);
        mImageButtonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDormitoryActivityTime();
            }
        });

        mLinearLayoutCheckBox = (LinearLayout) mCoordinatorLayout.findViewById(R.id.linear_layout_post_dormitory_activity_check_box);

    }

    @Override
    protected void loadData() {
        updateDormitoryMemberList();
    }

    private void pickDormitoryActivityDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTextViewDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void pickDormitoryActivityTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mTextViewTime.setText(hourOfDay + ":" + minute);
            }
        }, hour, minutes, true);
        timePickerDialog.show();
    }

    //获取寝室成员列表
    private void updateDormitoryMemberList() {
        if (mSingletonDormitory.getMembers() == null || mSingletonDormitory.getMembers().isEmpty()) {
            AVQuery<AVObject> memberListQuery = new AVQuery<AVObject>("Dormitory");
            memberListQuery.whereEqualTo("dormitoryId", mSingletonDormitory.getId());
            memberListQuery.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        mSingletonDormitory
                                .setMembers((ArrayList) avObject.getList("dormitoryMembers"));
                        mMessage = new Message();
                        mMessage.what = REFRESH_MEMBER_LIST;
                        mHandler.sendMessage(mMessage);
                    }
                }
            });
        } else {
            mMessage = new Message();
            mMessage.what = REFRESH_MEMBER_LIST;
            mHandler.sendMessage(mMessage);
        }
    }

    //设置参与成员列表
    private void setPersonCheckBoxList() {
        Iterator it = mDormitoryMemberList.iterator();
        while (it.hasNext()) {
            String memberId = (String) it.next();
            AVQuery<AVObject> memberNameQuery = new AVQuery<AVObject>("_User");
            memberNameQuery.whereEqualTo("objectId", memberId);
            memberNameQuery.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        if (avObject != null) {
                            CheckBox checkBox = (CheckBox) getLayoutInflater().inflate(R.layout.check_box_post_activity_person, null);
                            checkBox.setText(avObject.getString("userNickName"));
                            mCheckBoxes.add(checkBox);
                            mLinearLayoutCheckBox.addView(checkBox);
                            mLoadFinishedCheckBoxCount++;
                            Log.e(mLoadCheckCount+"", ""+mLoadFinishedCheckBoxCount);
                            if (mLoadFinishedCheckBoxCount == mLoadCheckCount) {
                                mMessage = new Message();
                                mMessage.what = LOAD_CHECK_BOX_FINISH;
                                mHandler.sendMessage(mMessage);
                            }
                        }
                    }
                }
            });
        }
    }

}
