package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.avos.avoscloud.AVException;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.PhotoWallPhoto;
import com.fishpondking.android.drop.engine.PhotoWallPhotoLab;
import com.fishpondking.android.drop.fragment.PhotoFragment;
import com.fishpondking.android.drop.utils.BaseActivity;

import java.util.ArrayList;

/**
 * Author: FishpondKing
 * Date: 2017/3/7:10:28
 * Email: song511653502@gmail.com
 * Description:
 */

public class PhotoPagerActivity extends BaseActivity {

    public static final String PHOTO_POSITION = "photoPosition";

    private ViewPager mViewPager;

    private PhotoWallPhotoLab mPhotoWallPhotoLab;
    private ArrayList<PhotoWallPhoto> mPhotos;
    private int photoPosition;

    public static void activityStart(Context context, int photoPosition){
        Intent intent = new Intent(context, PhotoPagerActivity.class);
        intent.putExtra(PHOTO_POSITION, photoPosition);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mPhotoWallPhotoLab = PhotoWallPhotoLab.get(PhotoPagerActivity.this);
        mPhotos = mPhotoWallPhotoLab.getPhotos();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) throws AVException {
        setContentView(R.layout.activity_photo_pager);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_photo);

        photoPosition = getIntent().getIntExtra(PHOTO_POSITION,0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return PhotoFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mPhotos.size();
            }
        });

        mViewPager.setCurrentItem(photoPosition);
    }

    @Override
    protected void loadData() {

    }
}
