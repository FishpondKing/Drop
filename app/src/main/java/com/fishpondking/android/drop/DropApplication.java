package com.fishpondking.android.drop;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Author: FishpondKing
 * Date: 2016/11/13:9:41
 * Email: song511653502@gmail.com
 * Description:
 */

public class DropApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置LeanCould的Application ID和Key
        AVOSCloud.initialize(this, "QrvyraLTyYqgzBpWslYTbCku-gzGzoHsz", "v1MyVnU1nhBpWAa4KaVGqSDH");
    }
}
