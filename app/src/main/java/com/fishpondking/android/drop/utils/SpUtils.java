package com.fishpondking.android.drop.utils;

import android.content.Context;
import android.preference.PreferenceManager;

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
     * Description: 保存用户登录状态
     * Param: context 应用上下文 user 要保存的用户
     * Return: void
     * Author: FishpondKing
     * Date: 2016/12/6:16:56
     */

    public static void saveUserState(Context context, User user){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("userId", user.getId())
                .putString("userTel", user.getUserTel())
                .putString("userName", user.getUserName())
                .putBoolean("userIsLeader", user.isLeader())
                .putString("userDormitoryId",
                        user.getDormitoryId())
                .apply();
    }
}
