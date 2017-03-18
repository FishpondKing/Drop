package com.fishpondking.android.drop.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.utils.BaseActivity;
import com.fishpondking.android.drop.utils.GlideImageLoader;
import com.fishpondking.android.drop.utils.RegexUtils;
import com.fishpondking.android.drop.utils.SpUtils;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fishpondking.android.drop.activity.WriteDiaryActivity.PERMISSIONS_REQUEST_READ_CONTACTS;

/**
 * Author: FishpondKing
 * Date: 2017/3/13:19:06
 * Email: song511653502@gmail.com
 * Description:
 */

public class UserCenterActivity extends BaseActivity {

    public static final String TAG = UserCenterActivity.class.getSimpleName();
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static final int REFRESH_USERHEAD = 61;
    public static final int REFRESH_USERNAME = 62;
    public static final int REFRESH_DORMITORY_LIFE_INFO = 63;

    private Activity mActivity;
    private Toolbar mToolbar;
    private CircleImageView mCircleImageViewUserHeader;
    private Button mButtonChangeUserHead;
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewUserId;
    private TextView mTextViewUserName;
    private ImageButton mImageButtonChangeUserName;
    private AlertDialog mDialogChangeUsername;
    private EditText mEditTextChangUsername;
    private TextView mTextViewJoinTime;
    private TextView mTextViewDiaryCount;
    private TextView mTextViewPhotoCount;

    private SingletonUser mSingletonUser;
    private List<String> path = new ArrayList<>();
    private Handler mHandler;
    private Message mMessage;
    private Date mJoinTime;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private int mDiaryCount;
    private int mPhotoCount;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mActivity = UserCenterActivity.this;
        mSingletonUser = SingletonUser.getInstance();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case REFRESH_USERHEAD:
                        updateUserhead();
                        break;
                    case REFRESH_USERNAME:
                        mTextViewUserName.setText(mSingletonUser.getUserName());
                        break;
                    case REFRESH_DORMITORY_LIFE_INFO:
                        mTextViewJoinTime.setText(mDateFormat.format(mJoinTime));
                        mTextViewDiaryCount.setText(String.valueOf(mDiaryCount));
                        mTextViewPhotoCount.setText(String.valueOf(mPhotoCount));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        setContentView(R.layout.activity_user_center);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_user_center);

        //配置ToolBar
        //解决打开ScrollView时显示在中间的问题
        mToolbar.setFocusable(true);
        mToolbar.setFocusableInTouchMode(true);
        mToolbar.requestFocus();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //显示用户头像
        mCircleImageViewUserHeader =
                (CircleImageView) findViewById(R.id.circle_image_view_user_info_head);

        //修改用户头像
        mButtonChangeUserHead = (Button) findViewById(R.id.button_user_info_change_head);
        initGallery();
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(path)                         // 记录已选的图片
                .multiSelect(false)                     // 是否多选   默认：false
                .crop(true)                            // 快捷开启裁剪功能，仅当单选或直接开启相机时有效
                .crop(true, 1, 1, 500, 500)           // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Drop/Gallery/UserHeader")     // 图片存放路径
                .build();
        mButtonChangeUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPermissions();
            }
        });

        //配置进度条
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(this.getResources().getString(R.string.userhead_submitting));

        mTextViewUserId = (TextView) findViewById(R.id.text_view_user_info_userId);
        mTextViewUserName = (TextView) findViewById(R.id.text_view_user_info_username);

        //配置修改用户名对话框
        mEditTextChangUsername = new EditText(mActivity);
        mDialogChangeUsername = new AlertDialog.Builder(mActivity).setTitle("请输入新的用户名")
                .setIcon(R.drawable.ic_user)
                .setView(mEditTextChangUsername)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUsername(mEditTextChangUsername);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        //修改用户名
        mImageButtonChangeUserName =
                (ImageButton) findViewById(R.id.image_button_user_info_change_username);
        mImageButtonChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogChangeUsername.show();
            }
        });

        mTextViewJoinTime = (TextView) findViewById(R.id.text_view_user_info_join_time);
        mTextViewDiaryCount = (TextView) findViewById(R.id.text_view_user_info_diary_count);
        mTextViewPhotoCount = (TextView) findViewById(R.id.text_view_user_info_photo_count);
    }

    @Override
    protected void loadData() {
        //显示用户信息
        updateUserhead();
        mTextViewUserId.setText(mSingletonUser.getId());
        mTextViewUserName.setText(mSingletonUser.getUserName());
        updateDormitoryLifeInfo();
    }

    //图片选择器监听接口
    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<String> photoList) {
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                //上传头像到数据库
                try {
                    submitUserHeadToDB(path);
                } catch (FileNotFoundException e) {
                    //失败，无法在本地找到该图片
                    Toast.makeText(getApplication(), R.string.cant_find_local_photo,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {

            }
        };
    }

    //授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "不需要授权 ");
            // 进行正常操作
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "同意授权");
                // 进行正常操作。
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
                Log.i(TAG, "拒绝授权");
            }
        }
    }

    private void submitUserHeadToDB(List<String> path) throws FileNotFoundException {
        //更新UserHead
        //取得该图片的后缀名
        String[] location = path.get(0).split("\\.");
        String suffix = location[location.length - 1];
        final String userHeadName = "userHead" + mSingletonUser.getId() + "." + suffix;
        final AVFile userHeadPhoto = AVFile.withAbsoluteLocalPath(userHeadName, path.get(0));
        mProgressDialog.show();

        //删除旧的头像
        AVQuery<AVObject> oldUserPhotoHead = new AVQuery<AVObject>("_File");
        oldUserPhotoHead.whereEqualTo("name", userHeadName);
        oldUserPhotoHead.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                //没有旧头像
                if(avObject == null){

                    //上传新的头像
                    userHeadPhoto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                //成功
                                Toast.makeText(getApplication(), R.string.change_userhead_success,
                                        Toast.LENGTH_SHORT)
                                        .show();
                                //获取用户头像的Url
                                //保存用户头像的Url并更新用户头像
                                AVQuery<AVObject> photoUrlQuery = new AVQuery<AVObject>("_File");
                                photoUrlQuery.whereEqualTo("name", userHeadName);
                                photoUrlQuery.getFirstInBackground(new GetCallback<AVObject>() {
                                    @Override
                                    public void done(AVObject avObject, AVException e) {
                                        mSingletonUser
                                                .setUserHeadPhotoUrl(avObject.getString("url"));
                                        mMessage = new Message();
                                        mMessage.what = REFRESH_USERHEAD;
                                        mHandler.sendMessage(mMessage);
                                        SpUtils.saveUserState(mActivity,mSingletonUser);
                                    }
                                });

                            } else {
                                // 失败，请检查网络环境以及 SDK 配置是否正确
                                Toast.makeText(getApplication(), R.string.change_userhead_fail,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            //上传进度数据，integer介于0和100之间
                            if (integer.equals(100)) {
                                mProgressDialog.cancel();
                            }
                        }
                    });

                }else {
                    avObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(AVException e) {
                            //上传新的头像
                            userHeadPhoto.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        //成功
                                        Toast.makeText(getApplication(), R.string.change_userhead_success,
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        //获取用户头像的Url
                                        //保存用户头像的Url并更新用户头像
                                        AVQuery<AVObject> photoUrlQuery = new AVQuery<AVObject>("_File");
                                        photoUrlQuery.whereEqualTo("name", userHeadName);
                                        photoUrlQuery.getFirstInBackground(new GetCallback<AVObject>() {
                                            @Override
                                            public void done(AVObject avObject, AVException e) {
                                                mSingletonUser
                                                        .setUserHeadPhotoUrl(avObject.getString("url"));
                                                mMessage = new Message();
                                                mMessage.what = REFRESH_USERHEAD;
                                                mHandler.sendMessage(mMessage);
                                                SpUtils.saveUserState(mActivity,mSingletonUser);
                                            }
                                        });

                                    } else {
                                        // 失败，请检查网络环境以及 SDK 配置是否正确
                                        Toast.makeText(getApplication(), R.string.change_userhead_fail,
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            }, new ProgressCallback() {
                                @Override
                                public void done(Integer integer) {
                                    //上传进度数据，integer介于0和100之间
                                    if (integer.equals(100)) {
                                        mProgressDialog.cancel();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    //更新用户头像
    private void updateUserhead(){
        SimpleTarget target = new SimpleTarget() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                mCircleImageViewUserHeader.setImageBitmap((Bitmap) resource);
            }
        };
        Glide.with(mActivity)
                .load(mSingletonUser.getUserHeadPhotoUrl())
                .asBitmap()
                .into(target);
    }

    //更改用户名
    private void updateUsername(EditText editText){
        //从EditText中获取新的用户名
        final String username = editText.getText().toString();

        //检查用户名格式是否符合规则
        if (RegexUtils.isUsername(username) == false) {
            Toast.makeText(mActivity, mActivity.getResources()
                    .getString(R.string.user_name_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

        //更新新的用户名
        AVQuery<AVObject> avUser = new AVQuery<AVObject>("_User");
        avUser.whereEqualTo("username", mSingletonUser.getId());
        avUser.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    avObject.put("userNickName", username);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                //成功
                                Toast.makeText(getApplication(), R.string.change_username_success,
                                        Toast.LENGTH_SHORT)
                                        .show();
                                mSingletonUser.setUserName(username);
                                mMessage = new Message();
                                mMessage.what = REFRESH_USERNAME;
                                mHandler.sendMessage(mMessage);
                                mDialogChangeUsername.cancel();
                                SpUtils.saveUserState(mActivity,mSingletonUser);

                                //更新用户所写日志所发照片的用户名
                                updateDiaryUsername();
                                updatePhotoUsername();

                            }else {
                                // 失败，请检查网络环境以及 SDK 配置是否正确
                                Toast.makeText(getApplication(), R.string.change_username_fail,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }else {
                    // 失败，请检查网络环境以及 SDK 配置是否正确
                    Toast.makeText(getApplication(), R.string.link_network_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    //更新用户宿舍生活信息
    private void updateDormitoryLifeInfo(){
        AVQuery<AVObject> avUser = new AVQuery<AVObject>("_User");
        avUser.whereEqualTo("username", mSingletonUser.getId());
        avUser.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null){
                    if(avObject != null){
                        mJoinTime = avObject.getDate("createdAt");
                        mDiaryCount = avObject.getInt("diaryCount");
                        Log.d(TAG, ""+mDiaryCount);
                        mPhotoCount = avObject.getInt("photoCount");
                        mMessage = new Message();
                        mMessage.what = REFRESH_DORMITORY_LIFE_INFO;
                        mHandler.sendMessage(mMessage);
                    }
                }else {
                    // 失败，请检查网络环境以及 SDK 配置是否正确
                    Toast.makeText(getApplication(), R.string.link_network_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    //更新用户所写Diary的用户名
    private void updateDiaryUsername(){
        AVQuery<AVObject> queryDiary = new AVQuery<>("Diary");
        queryDiary.whereEqualTo("authorId", mSingletonUser.getId());
        queryDiary.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                //该用户还没有写日志
                if(list.size() == 0){
                    return;
                }
                Iterator it = list.iterator();
                while (it.hasNext()){
                    AVObject avDiary = (AVObject) it.next();
                    avDiary.put("author", mSingletonUser.getUserName());
                    avDiary.saveInBackground();
                }
            }
        });
    }

    //更新用户所写Photo的用户名
    private void updatePhotoUsername(){
        AVQuery<AVObject> queryPhoto = new AVQuery<>("Photo");
        queryPhoto.whereEqualTo("authorId", mSingletonUser.getId());
        queryPhoto.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                //该用户还没有写日志
                if(list.size() == 0){
                    return;
                }
                Iterator it = list.iterator();
                while (it.hasNext()){
                    AVObject avPhoto = (AVObject) it.next();
                    avPhoto.put("author", mSingletonUser.getUserName());
                    avPhoto.saveInBackground();
                }
            }
        });
    }

}
