package com.fishpondking.android.drop.engine;

/**
 * Author: FishpondKing
 * Date: 2016/12/3:10:40
 * Email: song511653502@gmail.com
 * Description:
 */

public class SingletonDormitory extends Dormitory{

    private static SingletonDormitory sSingletonDormitory = null;

    private SingletonDormitory(){}

    public static SingletonDormitory getInstance(){
        if (sSingletonDormitory == null){
            sSingletonDormitory = new SingletonDormitory();
        }
        return sSingletonDormitory;
    }


}
