package com.fishpondking.android.drop.engine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.fragment.PhotoWallFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2017/3/5:14:53
 * Email: song511653502@gmail.com
 * Description:
 */

public class PhotoWallPhotoLab {

    private static PhotoWallPhotoLab sPhotoWallPhotoLab;

    private static Activity mActivity;

    private static ArrayList<PhotoWallPhoto> mPhotos;
    private static int totalItemCount;
    private static int previousItemCount;

    public static PhotoWallPhotoLab get(Context context){
        if(sPhotoWallPhotoLab == null){
            sPhotoWallPhotoLab = new PhotoWallPhotoLab(context);
        }
        return sPhotoWallPhotoLab;
    }

    private PhotoWallPhotoLab(Context context){
        mActivity = (Activity) context;
        mPhotos = new ArrayList<PhotoWallPhoto>();
    }

    //上拉加载PhotoWallPhotoLab
    public void pullUpRefreshPhotoWallPhotoLab(final Handler handler){
        //刷新DiaryLab时有此方法，下拉加载更多时无此方法
        //mPhotos.clear();
        SingletonDormitory singletonDormitory = SingletonDormitory.getInstance();
        //获取此宿舍的照片墙照片列表
        AVQuery<AVObject> query = new AVQuery<>("Photo");
        query.orderByDescending("createdAt");
        query.whereEqualTo("dormitoryId", singletonDormitory.getId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                //该宿舍照片墙没有照片
                if(list.size() == 0){
                    return;
                }
                //获取已经加载的Diary的总数量
                if (mPhotos == null){
                    totalItemCount = 0;
                }else {
                    totalItemCount = mPhotos.size();
                }
                previousItemCount = totalItemCount;

                //向UI线程发送照片的总数量和已经加载的照片的数量
                Message messageDiaryCount = new Message();
                messageDiaryCount.what = PhotoWallFragment.PHOTO_LIST_SIZE;
                messageDiaryCount.arg1 = list.size();
                messageDiaryCount.arg2 = previousItemCount;
                handler.sendMessage(messageDiaryCount);

                //加载每一张照片
                if (totalItemCount < list.size()){
                    int i = 0;
                    while (i<PhotoWallFragment.EACH_PAGE_PHOTO_COUNT && totalItemCount<list.size()){
                        //加载每一张照片
                        AVObject avPhoto = (AVObject) list.get(previousItemCount + i);

                        final PhotoWallPhoto photoWallPhoto = new PhotoWallPhoto();
                        photoWallPhoto.setId(avPhoto.getString("photoId"));
                        photoWallPhoto.setAuthor(avPhoto.getString("author"));
                        photoWallPhoto.setAuthorId(avPhoto.getString("authorId"));
                        photoWallPhoto.setDormitoryId(avPhoto.getString("dormitoryId"));
                        photoWallPhoto.setDate(avPhoto.getDate("createdAt"));
                        photoWallPhoto.setDescription(avPhoto.getString("description"));
                        mPhotos.add(photoWallPhoto);

                        //保存照片Url
                        String photoName = avPhoto.getString("photoName");
                        AVQuery<AVObject> photoQuery = new AVQuery<AVObject>("_File");
                        photoQuery.whereEqualTo("name", photoName);
                        photoQuery.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if(avObject != null){
                                    photoWallPhoto.setPhotoUrl(avObject.getString("url"));
                                    //添加照片到照片墙
                                    //通过Glide加载
                                    //每一张照片下载完后向UI线程通知
                                    SimpleTarget simpleTarget = new SimpleTarget() {
                                        @Override
                                        public void onResourceReady(Object resource,
                                                                    GlideAnimation glideAnimation) {
                                            Message messageAddPhoto = new Message();
                                            messageAddPhoto.what =
                                                    PhotoWallFragment.ONE_PHOTO_DOWNLOAD_SUCCESS;
                                            handler.sendMessage(messageAddPhoto);
                                        }
                                    };
                                    Glide.with(mActivity)
                                            .load(avObject.getString("url"))
                                            .into(simpleTarget);
                                }
                            }
                        });
                        totalItemCount++;
                        i++;
                    }
                }


//                Iterator iterator = list.iterator();
//                while (iterator.hasNext()){
//                    //加载照片墙的每一张照片
//                    AVObject avObject = (AVObject) iterator.next();
//                    final PhotoWallPhoto photoWallPhoto = new PhotoWallPhoto();
//                    photoWallPhoto.setId(avObject.getString("photoId"));
//                    photoWallPhoto.setAuthor(avObject.getString("author"));
//                    photoWallPhoto.setAuthorId(avObject.getString("authorId"));
//                    photoWallPhoto.setDormitoryId(avObject.getString("dormitoryId"));
//                    photoWallPhoto.setDate(avObject.getDate("createdAt"));
//                    photoWallPhoto.setDescription(avObject.getString("description"));
//                    mPhotos.add(photoWallPhoto);
//
//                    //保存照片Url
//                    String photoName = avObject.getString("photoName");
//                    AVQuery<AVObject> photoQuery = new AVQuery<AVObject>("_File");
//                    photoQuery.whereEqualTo("name", photoName);
//                    photoQuery.getFirstInBackground(new GetCallback<AVObject>() {
//                        @Override
//                        public void done(AVObject avObject, AVException e) {
//                            photoWallPhoto.setPhotoUrl(avObject.getString("url"));
//                            //添加照片到照片墙
//                            //每一张照片下载完后向UI线程通知
//                            Message messageAddPhoto = new Message();
//                            messageAddPhoto.what = PhotoWallFragment.ONE_PHOTO_DOWNLOAD_SUCCESS;
//                            handler.sendMessage(messageAddPhoto);
//                        }
//                    });
//
//                }
            }
        });
    }

    //下拉刷新PhotoWallPhotoLab
    public void pullDownRefreshPhotoWallPhotoLab(final Handler handler){
        mPhotos.clear();
        Message msg = new Message();
        msg.what = PhotoWallFragment.REQUEST_REFRESH_PHOTO_WALL;
        handler.sendMessage(msg);
        pullUpRefreshPhotoWallPhotoLab(handler);
    }

    //获取照片列表
    public ArrayList<PhotoWallPhoto> getPhotos(Handler handler){
        if(mPhotos == null || mPhotos.isEmpty()){
            pullDownRefreshPhotoWallPhotoLab(handler);
            return mPhotos;
        }else {
            return getPhotos();
        }
    }

    public static ArrayList<PhotoWallPhoto> getPhotos() {
        return mPhotos;
    }

    public PhotoWallPhoto getPhoto(int position){
        if (mPhotos.size() > position){
            return mPhotos.get(position);
        }else {
            return null;
        }
    }

    //增加照片
    public void addPhoto(PhotoWallPhoto photo){
        mPhotos.add(photo);
    }
}
