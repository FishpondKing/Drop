package com.fishpondking.android.drop.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.fishpondking.android.drop.fragment.DiaryFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:20:17
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryLab {

    private static DiaryLab sDiaryLab;

    private static ArrayList<Diary> mDiaries;

    private static int totalItemCount;
    private static int previousItemCount;

    public static DiaryLab get(Context context){
        if (sDiaryLab == null){
            sDiaryLab = new DiaryLab(context);
        }
        return sDiaryLab;
    }

    private DiaryLab(Context context){
        mDiaries = new ArrayList<Diary>();
    }

    //上拉加载DiaryLab
    public void pullUpRefreshDiaryLab(final Handler handler){
        //刷新DiaryLab时有此方法，下拉加载更多时无此方法
        //mDiaries.clear();
        SingletonDormitory singletonDormitory = SingletonDormitory.getInstance();
        //获取此宿舍的日志列表
        AVQuery<AVObject> query = new AVQuery<>("Diary");
        query.orderByDescending("createdAt");
        query.whereEqualTo("dormitoryId", singletonDormitory.getId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                //该宿舍没有日志
                if(list.size() == 0){
                    return;
                }
                //获取已经加载的Diary的总数量
                if (mDiaries == null){
                    totalItemCount = 0;
                }else {
                    totalItemCount = mDiaries.size();
                }
                previousItemCount = totalItemCount;

                //向UI线程发送日志的总数量和已经加载的数量
                Message messageDiaryCount = new Message();
                messageDiaryCount.what = DiaryFragment.DIARY_LIST_SIZE;
                messageDiaryCount.arg1 = list.size();
                messageDiaryCount.arg2 = previousItemCount;
                handler.sendMessage(messageDiaryCount);

                //加载每一篇日志
                if (totalItemCount < list.size()){
                    int i = 0;
                    while (i<DiaryFragment.EACH_PAGE_DIARY_COUNT && totalItemCount<list.size()){
                        //加载每一篇日志
                        AVObject avDiary = (AVObject) list.get(previousItemCount + i);

                        final Diary diary = new Diary();
                        diary.setId(avDiary.getString("diaryId"));
                        diary.setAuthor(avDiary.getString("author"));
                        diary.setAuthorId(avDiary.getString("authorId"));
                        diary.setTitle(avDiary.getString("diaryTitle"));
                        diary.setContent(avDiary.getString("content"));
                        diary.setDormitoryId(avDiary.getString("dormitoryId"));
                        diary.setDate(avDiary.getDate("createdAt"));
                        mDiaries.add(diary);

                        //保存日志TopPhotoUrl
                        String diaryTopPhotoName = "diaryTopPhoto" + avDiary.getString("diaryId")
                                + ".jpg";
                        AVQuery<AVObject> topPhotoQuery = new AVQuery<AVObject>("_File");
                        topPhotoQuery.whereEqualTo("name", diaryTopPhotoName);
                        topPhotoQuery.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if(avObject != null){
                                    diary.setTopPhotoUrl(avObject.getString("url"));
                                    //添加日志到日志列表
                                    //每一张日志图片下载完后向通知UI线程
                                    Message messageAddDiaryTopPhoto = new Message();
                                    messageAddDiaryTopPhoto.what =
                                            DiaryFragment.ONE_DIARY_DOWNLOAD_SUCCESS;
                                    handler.sendMessage(messageAddDiaryTopPhoto);
                                }
                            }
                        });
                        totalItemCount++;
                        i++;
                    }
                }
            }
        });
    }

    //下拉刷新DiaryLab
    public void pullDownRefreshDiaryLab(final Handler handler){
        mDiaries.clear();
        Message msg = new Message();
        msg.what = DiaryFragment.REQUEST_REFRESH_DIARY_LIST;
        handler.sendMessage(msg);
        pullUpRefreshDiaryLab(handler);
    }

    //获取日志列表
    public ArrayList<Diary> getDiaries(Handler handler){
        if (mDiaries == null || mDiaries.isEmpty()){
            pullDownRefreshDiaryLab(handler);
            return mDiaries;
        }
        else {
            return getDiaries();
        }
    }

    public ArrayList<Diary> getDiaries(){
        return mDiaries;
    }


    public Diary getDiary(String id){
        for (Diary diary : mDiaries){
            if (diary.getId().equals(id)){
                return diary;
            }
        }
        return null;
    }

    //增加日志
    public void addDiary(Diary diary){
        mDiaries.add(diary);
    }
}
