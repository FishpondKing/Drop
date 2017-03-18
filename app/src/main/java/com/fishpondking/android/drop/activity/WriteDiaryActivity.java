package com.fishpondking.android.drop.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.Diary;
import com.fishpondking.android.drop.engine.DiaryLab;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.fragment.DiaryFragment;
import com.fishpondking.android.drop.utils.BaseActivity;
import com.fishpondking.android.drop.utils.GlideImageLoader;
import com.fishpondking.android.drop.utils.RegexUtils;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2017/2/12:10:49
 * Email: song511653502@gmail.com
 * Description:
 */

public class WriteDiaryActivity extends BaseActivity {

    public static final String TAG = WriteDiaryActivity.class.getSimpleName();
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static final int RESULT_REFRESH = 1;

    private Toolbar mToolbar;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private ProgressDialog mProgressDialog;

    private Diary mDiary;
    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private DiaryLab mDiaryLab;
    private List<String> path = new ArrayList<>();
    private IHandlerCallBack iHandlerCallBack;
    private GalleryConfig galleryConfig;
    private DecimalFormat df = new DecimalFormat("000000");
    private int mOldDiaryCount;
    private String mDiaryCount;
    private int mOldUserDiaryCount;
    private int mOldDormitoryDiaryCount;
    private Activity mActivity;

    public static void activityStart(Context context, Fragment fragment) {
        Intent intent = new Intent(context, WriteDiaryActivity.class);
        fragment.startActivityForResult(intent, DiaryFragment.REQUEST_ADD_DIARY);
    }

    @Override
    protected void initVariables() {
        mDiary = new Diary();
        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();
        mActivity = WriteDiaryActivity.this;
        mDiaryLab = DiaryLab.get(mActivity);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        setContentView(R.layout.activity_write_diary);

        //配置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_write_diary);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextTitle = (EditText) findViewById(R.id.edit_text_diary_title);

        mEditTextContent = (EditText) findViewById(R.id.edit_text_diary_content);

        //初始化图片选择器
        initGallery();
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(path)                         // 记录已选的图片
                .multiSelect(false)                     // 是否多选   默认：false
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(true)                            // 快捷开启裁剪功能，仅当单选或直接开启相机时有效
                .crop(true, 4, 3, 500, 500)           // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Drop/Gallery/Pictures")     // 图片存放路径
                .build();

        //配置进度条
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(this.getResources().getString(R.string.diary_submitting));

    }

    @Override
    protected void loadData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_write_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //提交按钮
            case R.id.menu_submit_diary:
                //创建并上传Diary，并添加到DiaryLab
                //选择照片
                //判断DiaryTitle中不含空字符
                if (RegexUtils.isStringNotContainSpace(mEditTextTitle.getText().toString()) == true
                        && mEditTextTitle.getText().toString().isEmpty() == false) {
                    initPermissions();
                    return true;
                } else {
                    Toast.makeText(this, R.string.diary_title_wrong, Toast.LENGTH_SHORT).show();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
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

    //图片选择器监听接口
    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                //photoAdapter.notifyDataSetChanged();
                //初始化mDiary
                mDiary.clear();

                //更新UtilData表diaryCount
                AVQuery<AVObject> query = new AVQuery<>("UtilData");
                query.whereEqualTo("scope", "all");
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            //获取已存在的日志数量
                            mOldDiaryCount = avObject.getInt("diaryCount");
                            mOldDiaryCount++;
                            avObject.put("diaryCount", mOldDiaryCount);
                            avObject.saveInBackground();
                            mDiaryCount = df.format(mOldDiaryCount);

                            //更新此用户所写的日志数量
                            updateUserDiaryCountToDB();

                            //更新此宿舍的日志数量
                            updateDormitoryDiaryCountToDB();

                            try {
                                //上传日志
                                submitDiaryToDB(path);
                            } catch (FileNotFoundException e1) {
                                //失败，无法在本地找到DiaryTopPhoto
                                Toast.makeText(getApplication(), R.string.cant_find_local_photo,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                        } else {
                            // 失败，请检查网络环境以及 SDK 配置是否正确
                            Toast.makeText(getApplication(), R.string.submit_fail, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };

    }

    //添加日志内容到Diary表
    private void submitDiaryToDB(List<String> path) throws FileNotFoundException {
        //保存diary基本信息
        final AVObject diary = new AVObject("Diary");
        diary.put("diaryId", mDiaryCount);
        diary.put("diaryTitle", mEditTextTitle.getText().toString());
        diary.put("authorId", mSingletonUser.getId());
        diary.put("author", mSingletonUser.getUserName());
        diary.put("dormitoryId", mSingletonDormitory.getId());
        diary.put("content", mEditTextContent.getText().toString());
        diary.saveInBackground();
        //保存diary图片
        //取得该图片的后缀名
        String[] location = path.get(0).split("\\.");
        String suffix = location[location.length - 1];
        String diaryTopPhotoName = "diaryTopPhoto" + mDiaryCount + "." + suffix;
        AVFile diaryTopPhoto = AVFile.withAbsoluteLocalPath(diaryTopPhotoName, path.get(0));
        //显示ProgressDialog
        mProgressDialog.show();
        //上传diaryTopPhoto
        diaryTopPhoto.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //成功
                    Toast.makeText(getApplication(), R.string.submit_success,
                            Toast.LENGTH_SHORT)
                            .show();
                    //返回刷新指令，关闭写日志界面
                    setResult(RESULT_REFRESH);
                    mActivity.finish();
                } else {
                    // 失败，请检查网络环境以及 SDK 配置是否正确
                    Toast.makeText(getApplication(), R.string.submit_diary_top_photo_wrong,
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

    //更新_User表diaryCount
    private void updateUserDiaryCountToDB() {
        mOldUserDiaryCount = AVUser.getCurrentUser().getInt("diaryCount");
        mOldUserDiaryCount++;
        AVUser.getCurrentUser().put("diaryCount", mOldUserDiaryCount);
        AVUser.getCurrentUser().saveInBackground();
    }

    //更新Dormitory表diaryCount
    private void updateDormitoryDiaryCountToDB() {
        AVQuery<AVObject> query = new AVQuery<>("Dormitory");
        query.whereEqualTo("dormitoryId", mSingletonDormitory.getId());
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mOldDormitoryDiaryCount = avObject.getInt("diaryCount");
                mOldDormitoryDiaryCount++;
                avObject.put("diaryCount", mOldDormitoryDiaryCount);
                avObject.saveInBackground();
            }
        });
    }
}
