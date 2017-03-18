package com.fishpondking.android.drop.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.LoginAndSigninActivity;
import com.fishpondking.android.drop.activity.UserCenterActivity;
import com.fishpondking.android.drop.engine.Roommate;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.SpUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fishpondking.android.drop.utils.SpUtils.clearUserState;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:17:15
 * Email: song511653502@gmail.com
 * Description: 主界面中“更多”选项卡对应的界面
 */

public class MoreOptionsFragment extends Fragment {

    public static final int REFRESH_MEMBER_LIST = 51;

    public static final int ADD_ROOMMATE_0 = 500;
    public static final int ADD_ROOMMATE_1 = 501;
    public static final int ADD_ROOMMATE_2 = 502;
    public static final int ADD_ROOMMATE_3 = 503;
    public static final int ADD_ROOMMATE_4 = 504;
    public static final int ADD_ROOMMATE_5 = 505;
    public static final int ADD_ROOMMATE_6 = 506;
    public static final int ADD_ROOMMATE_7 = 507;

    private View mView;
    private Activity mActivity;
    private CircleImageView mCircleImageViewUserHead;
    private TextView mTextViewUsername;
    private ImageButton mImageButtonUserCenter;
    private ViewStub mViewStub0;
    private ViewStub mViewStub1;
    private ViewStub mViewStub2;
    private ViewStub mViewStub3;
    private ViewStub mViewStub4;
    private ViewStub mViewStub5;
    private ViewStub mViewStub6;
    private ViewStub mViewStub7;
    private CircleImageView mCircleImageViewRoommate0;
    private TextView mTextViewRoommate0;
    private CircleImageView mCircleImageViewRoommate1;
    private TextView mTextViewRoommate1;
    private CircleImageView mCircleImageViewRoommate2;
    private TextView mTextViewRoommate2;
    private CircleImageView mCircleImageViewRoommate3;
    private TextView mTextViewRoommate3;
    private CircleImageView mCircleImageViewRoommate4;
    private TextView mTextViewRoommate4;
    private CircleImageView mCircleImageViewRoommate5;
    private TextView mTextViewRoommate5;
    private CircleImageView mCircleImageViewRoommate6;
    private TextView mTextViewRoommate6;
    private CircleImageView mCircleImageViewRoommate7;
    private TextView mTextViewRoommate7;
    private Button mButtonFeedback;
    private Button mButtonLogout;

    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private ArrayList<String> mDormitoryMemberList;
    private Handler mHandler;
    private Message mMessage;
    private int mLoadRoommateNumber;
    private int mLoadRoommatePhotoNumber;
    private Roommate mRoommate0;
    private Roommate mRoommate1;
    private Roommate mRoommate2;
    private Roommate mRoommate3;
    private Roommate mRoommate4;
    private Roommate mRoommate5;
    private Roommate mRoommate6;
    private Roommate mRoommate7;
    private Lock mLock = new ReentrantLock();

    public static MoreOptionsFragment newInstance() {
        MoreOptionsFragment moreOptionsFragment = new MoreOptionsFragment();
        return moreOptionsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more_options, container, false);
        mActivity = getActivity();
        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_MEMBER_LIST:
                        mDormitoryMemberList = mSingletonDormitory.getMembers();
                        setRoomatesInfo();
                        break;
                    case ADD_ROOMMATE_0:
                        mRoommate0 = new Roommate();
                        mRoommate0 = (Roommate) msg.obj;
                        mViewStub0.inflate();
                        mCircleImageViewRoommate0 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_0);
                        mTextViewRoommate0 = (TextView) mView.findViewById(R.id.text_view_roommate_name_0);
                        mTextViewRoommate0.setText(mRoommate0.getName());
                        if (mRoommate0.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate0.getHeadPhotoUrl(), mCircleImageViewRoommate0);
                        }
                        break;
                    case ADD_ROOMMATE_1:
                        mRoommate1 = new Roommate();
                        mRoommate1 = (Roommate) msg.obj;
                        mViewStub1.inflate();
                        mCircleImageViewRoommate1 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_1);
                        mTextViewRoommate1 = (TextView) mView.findViewById(R.id.text_view_roommate_name_1);
                        mTextViewRoommate1.setText(mRoommate1.getName());
                        if (mRoommate1.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate1.getHeadPhotoUrl(), mCircleImageViewRoommate1);
                        }
                        break;
                    case ADD_ROOMMATE_2:
                        mRoommate2 = new Roommate();
                        mRoommate2 = (Roommate) msg.obj;
                        mViewStub2.inflate();
                        mCircleImageViewRoommate2 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_2);
                        mTextViewRoommate2 = (TextView) mView.findViewById(R.id.text_view_roommate_name_2);
                        mTextViewRoommate2.setText(mRoommate2.getName());
                        if (mRoommate2.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate2.getHeadPhotoUrl(), mCircleImageViewRoommate2);
                        }
                        break;
                    case ADD_ROOMMATE_3:
                        mRoommate3 = new Roommate();
                        mRoommate3 = (Roommate) msg.obj;
                        mViewStub3.inflate();
                        mCircleImageViewRoommate3 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_3);
                        mTextViewRoommate3 = (TextView) mView.findViewById(R.id.text_view_roommate_name_3);
                        mTextViewRoommate3.setText(mRoommate3.getName());
                        if (mRoommate3.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate3.getHeadPhotoUrl(), mCircleImageViewRoommate3);
                        }
                        break;
                    case ADD_ROOMMATE_4:
                        mRoommate4 = new Roommate();
                        mRoommate4 = (Roommate) msg.obj;
                        mViewStub4.inflate();
                        mCircleImageViewRoommate4 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_4);
                        mTextViewRoommate4 = (TextView) mView.findViewById(R.id.text_view_roommate_name_4);
                        mTextViewRoommate4.setText(mRoommate0.getName());
                        if (mRoommate4.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate4.getHeadPhotoUrl(), mCircleImageViewRoommate4);
                        }
                        break;
                    case ADD_ROOMMATE_5:
                        mRoommate5 = new Roommate();
                        mRoommate5 = (Roommate) msg.obj;
                        mViewStub5.inflate();
                        mCircleImageViewRoommate5 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_5);
                        mTextViewRoommate5 = (TextView) mView.findViewById(R.id.text_view_roommate_name_5);
                        mTextViewRoommate5.setText(mRoommate0.getName());
                        if (mRoommate5.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate5.getHeadPhotoUrl(), mCircleImageViewRoommate5);
                        }
                        break;
                    case ADD_ROOMMATE_6:
                        mRoommate6 = new Roommate();
                        mRoommate6 = (Roommate) msg.obj;
                        mViewStub6.inflate();
                        mCircleImageViewRoommate6 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_6);
                        mTextViewRoommate6 = (TextView) mView.findViewById(R.id.text_view_roommate_name_6);
                        mTextViewRoommate6.setText(mRoommate0.getName());
                        if (mRoommate6.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate6.getHeadPhotoUrl(), mCircleImageViewRoommate6);
                        }
                        break;
                    case ADD_ROOMMATE_7:
                        mRoommate7 = new Roommate();
                        mRoommate7 = (Roommate) msg.obj;
                        mViewStub7.inflate();
                        mCircleImageViewRoommate7 = (CircleImageView) mView.findViewById(R.id.circle_image_view_roommate_head_7);
                        mTextViewRoommate7 = (TextView) mView.findViewById(R.id.text_view_roommate_name_7);
                        mTextViewRoommate7.setText(mRoommate0.getName());
                        if (mRoommate7.getHeadPhotoUrl() != null) {
                            updateHeadPhoto(mRoommate7.getHeadPhotoUrl(), mCircleImageViewRoommate7);
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        //获取寝室成员列表
        updateDormitoryMemberList();

        mCircleImageViewUserHead =
                (CircleImageView) mView.findViewById(R.id.circle_image_view_more_options_user_head);
        updateHeadPhoto(mSingletonUser.getUserHeadPhotoUrl(), mCircleImageViewUserHead);

        mTextViewUsername = (TextView) mView.findViewById(R.id.text_view_more_options_user_head);
        mTextViewUsername.setText(mSingletonUser.getUserName());

        mImageButtonUserCenter = (ImageButton) mView.findViewById(R.id.image_button_user_center);
        mImageButtonUserCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCenterActivity.activityStart(mActivity);
            }
        });

        //添加显示寝室成员信息的View
        addRoommatesView();

        //意见反馈按钮
        mButtonFeedback = (Button) mView.findViewById(R.id.button_more_options_feedback);
        mButtonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackAgent agent = new FeedbackAgent(mActivity);
                agent.startDefaultThreadActivity();
            }
        });

        //注销按钮
        mButtonLogout = (Button) mView.findViewById(R.id.button_more_options_logout);
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingletonUser.logOut();
                mSingletonUser.clear();
                mSingletonDormitory.clear();
                SpUtils.clearUserState(mActivity);
                SpUtils.clearDormitoryState(mActivity);
                LoginAndSigninActivity.activityStart(mActivity, 0);
                mActivity.finish();
            }
        });

        return mView;
    }

    //设置头像图片
    private void updateHeadPhoto(String photoUrl, final CircleImageView circleImageView) {
        SimpleTarget target = new SimpleTarget() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                circleImageView.setImageBitmap((Bitmap) resource);
            }
        };

        Glide.with(mActivity)
                .load(photoUrl)
                .asBitmap()
                .into(target);
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

    //显示寝室成员的头像昵称
    private void setRoomatesInfo() {
        mLoadRoommateNumber = 0;
        mLoadRoommatePhotoNumber = 0;
        for (mLoadRoommateNumber = 0; mLoadRoommateNumber < mDormitoryMemberList.size(); mLoadRoommateNumber++) {
            AVQuery<AVObject> avMember = new AVQuery<AVObject>("_User");
            avMember.whereEqualTo("objectId", mDormitoryMemberList.get(mLoadRoommateNumber));
            avMember.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        final Roommate roommate = new Roommate();
                        roommate.setId(avObject.getString("username"));
                        roommate.setName(avObject.getString("userNickName"));
                        String roommatePhotoName = "userHead" + roommate.getId() + ".jpg";
                        AVQuery<AVObject> roommatePhotoQuery = new AVQuery<AVObject>("_File");
                        Log.d("roommatePhotoName", roommatePhotoName.toString());
                        roommatePhotoQuery.whereEqualTo("name", roommatePhotoName);
                        roommatePhotoQuery.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if (e == null) {
                                    //该室友有头像
                                    if (avObject != null) {
                                        roommate.setHeadPhotoUrl(avObject.getString("url"));
                                        mLock.lock();
                                        try {
                                            Message msg = new Message();
                                            msg.what = Integer.parseInt("50" + mLoadRoommatePhotoNumber);
                                            mLoadRoommatePhotoNumber++;
                                            Log.d("有照片", "" + msg.what);
                                            msg.obj = roommate;
                                            mHandler.sendMessage(msg);
                                        } catch (Exception ex) {

                                        } finally {
                                            mLock.unlock();
                                        }

                                    } else {
                                        mLock.lock();
                                        try {
                                            Message msg = new Message();
                                            msg.what = Integer.parseInt("50" + mLoadRoommatePhotoNumber);
                                            mLoadRoommatePhotoNumber++;
                                            Log.d("无照片", "" + msg.what);
                                            msg.obj = roommate;
                                            mHandler.sendMessage(msg);
                                        } catch (Exception ex) {

                                        } finally {
                                            mLock.unlock();
                                        }

                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void addRoommatesView() {
        mViewStub0 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_0);
        mViewStub1 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_1);
        mViewStub2 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_2);
        mViewStub3 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_3);
        mViewStub4 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_4);
        mViewStub5 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_5);
        mViewStub6 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_6);
        mViewStub7 = (ViewStub) mView.findViewById(R.id.view_stub_more_options_roommate_7);
    }

}
