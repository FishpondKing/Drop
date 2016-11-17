package com.fishpondking.android.drop.engine;

import com.avos.avoscloud.AVUser;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:9:49
 * Email: song511653502@gmail.com
 * Description:
 */

public class User extends AVUser {

    public void setUserTel(String userTel){
        setMobilePhoneNumber(userTel);
    }

    public void setUserPassword(String userPassword, String userPasswordCheck){
        if(userPassword.equals(userPasswordCheck)){
            setPassword(userPassword);
        }
    }

    public void setUserName(String userName){
        setUsername(userName);
    }
}
