package com.fishpondking.android.drop.engine;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:20:17
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryLab {

    private static DiaryLab sDiaryLab;

    private List<Diary> mDiaries;

    public static DiaryLab get(Context context){
        if (sDiaryLab == null){
            sDiaryLab = new DiaryLab(context);
        }
        return sDiaryLab;
    }

    private DiaryLab(Context context){
        mDiaries = new ArrayList<>();
    }

    public Diary getDiary(String id){
        for (Diary diary : mDiaries){
            if (diary.getId().equals(id)){
                return diary;
            }
        }
        return null;
    }
}
