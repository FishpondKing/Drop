package com.fishpondking.android.drop.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing 
 * Date: 2016/11/3:10:51
 * Email: song511653502@gmail.com 
 * Description: 统一管理全部Activity
 */

public class ActivityCollector {

    public static List<Activity> sActivities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
