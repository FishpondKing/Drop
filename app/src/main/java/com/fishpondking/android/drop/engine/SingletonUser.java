package com.fishpondking.android.drop.engine;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:17:18
 * Email: song511653502@gmail.com
 * Description:
 */

public class SingletonUser extends User{

    private static SingletonUser sSingletonUser;

    private SingletonUser(){}

    public static SingletonUser getInstance(){
        if (sSingletonUser == null){
            sSingletonUser = new SingletonUser();
        }
        return sSingletonUser;
    }
}
