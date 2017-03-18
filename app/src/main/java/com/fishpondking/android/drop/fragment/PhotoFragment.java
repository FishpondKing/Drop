package com.fishpondking.android.drop.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.PhotoWallPhoto;
import com.fishpondking.android.drop.engine.PhotoWallPhotoLab;
import com.fishpondking.android.drop.ui.ZoomImageView;

/**
 * Author: FishpondKing
 * Date: 2017/3/7:10:49
 * Email: song511653502@gmail.com
 * Description:
 */

public class PhotoFragment extends Fragment {

    private static final String PHOTO_INDEX = "photo_index";

    private View mView;
    private ZoomImageView mZoomImageView;
    private ProgressDialog mProgressDialog;

    private PhotoWallPhotoLab mPhotoWallPhotoLab;
    private PhotoWallPhoto mPhotoWallPhoto;
    private SimpleTarget mTarget;

    public static PhotoFragment newInstance(int photoIndex) {
        Bundle args = new Bundle();
        args.putInt(PHOTO_INDEX, photoIndex);

        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(args);
        return photoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoWallPhotoLab = PhotoWallPhotoLab.get(getActivity());
        int photoIndex = getArguments().getInt(PHOTO_INDEX);
        mPhotoWallPhoto = mPhotoWallPhotoLab.getPhoto(photoIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photo, container, false);
        mZoomImageView = (ZoomImageView) mView.findViewById(R.id.zoom_image_view_photo_pager_photo);
        mTarget = new SimpleTarget() {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                mZoomImageView.setImageBitmap((Bitmap)resource);
                mProgressDialog.cancel();
            }
        };

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().finish();
                }
                return false;
            }
        });
        mProgressDialog.show();

        Glide.with(getActivity())
                .load(mPhotoWallPhoto.getPhotoUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_bitmap)
                .thumbnail(0.1f)
                .into(mTarget);
        return mView;
    }
}
