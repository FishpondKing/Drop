package com.fishpondking.android.drop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fishpondking.android.drop.engine.Dormitory;
import com.fishpondking.android.drop.engine.User;

/**
 * Author: FishpondKing
 * Date: 2016/12/6:16:53
 * Email: song511653502@gmail.com
 * Description:
 */

public class SpUtils {

    /**
     * Method: saveUserState(Context context, User user)
     * Description: 保存用户登录状态，包括用户ID，手机号，昵称，是否为舍长，宿舍Id
     * Param: context 应用上下文 user 要保存的用户
     * Return: void
     * Author: FishpondKing
     * Date: 2016/12/6:16:56
     */

    public static void saveUserState(Context context, User user) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("userId", user.getId())
                .putString("userTel", user.getUserTel())
                .putString("userName", user.getUserName())
                .putBoolean("userIsLeader", user.isLeader())
                .putString("userDormitoryId", user.getDormitoryId())
                .apply();
    }

    /**
     * Method: saveDormitoryState(Context context, Dormitory dormitory)
     * Description: 存储宿舍信息，包括宿舍Id，宿舍名，舍长
     * Param:   context 应用上下文 user 要保存的宿舍
     * Return:  void
     * Author: FishpondKing
     * Date: 2016/12/7:8:54
     */

    public static void saveDormitoryState(Context context, Dormitory dormitory) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("dormitoryId", dormitory.getId())
                .putString("dormitoryName", dormitory.getName())
                .putString("dormitoryLeader", dormitory.getLeader())
                .apply();
    }

    /**
     * Method: loadUserState(Context context, User user)
     * Description: 加载用户信息，包括用户Id，手机号，昵称，是否为舍长，宿舍Id
     * Param: context 应用上下文 user 要加载信息的用户
     * Return: void
     * Author: FishpondKing
     * Date: 2016/12/7:9:49
     */

    public static void loadUserState(Context context, User user) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        user.setId(sp.getString("userId", ""));
        user.setId(sp.getString("userTel", ""));
        user.setUserName(sp.getString("userName", ""));
        user.setLeader(sp.getBoolean("userIsLeader", false));
        user.setDormitoryId(sp.getString("userDormitoryId", ""));
    }

    /**
     * Method: loadDormitoryState(Context context, Dormitory dormitory)
     * Description: 加载宿舍信息，包括宿舍Id，宿舍名，舍长
     * Param: context 应用上下文 user 要加载信息的宿舍
     * Return: void
     * Author: FishpondKing
     * Date: 2016/12/7:9:50
     */

    public static void loadDormitoryState(Context context, Dormitory dormitory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        dormitory.setId(sp.getString("dormitoryId", ""));
        dormitory.setName(sp.getString("dormitoryName", ""));
        dormitory.setLeader(sp.getString("dormitoryLeader", ""));
    }
}
