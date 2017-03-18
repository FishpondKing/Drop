package com.fishpondking.android.drop.utils;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.fishpondking.android.drop.R;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

/**
 * Author: FishpondKing
 * Date: 2017/2/16:14:55
 * Email: song511653502@gmail.com
 * Description: 图片选择器的类加载器
 */

public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";

    @Override
    public void displayImage(Activity activity, Context context, String path,
                             GalleryImageView galleryImageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(galleryImageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
