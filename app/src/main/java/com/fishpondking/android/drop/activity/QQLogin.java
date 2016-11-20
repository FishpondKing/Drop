package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.fishpondking.android.drop.utils.BaseActivity;

import com.avos.sns.*;

/**
 * Author: FishpondKing
 * Date: 2016/11/20:10:16
 * Email: song511653502@gmail.com
 * Description:
 */

public class QQLogin extends BaseActivity {

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, QQLogin.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

    }

    @Override

    protected void initViews(Bundle savedInstanceState) throws AVException {

        // callback 函数
        final SNSCallback myCallback = new SNSCallback() {
            @Override
            public void done(SNSBase object, SNSException e) {
                if (e == null) {
                    Toast.makeText(getApplication(), "login ok " + SNSType.AVOSCloudSNSQQ,
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 关联
        SNS.setupPlatform(this, SNSType.AVOSCloudSNSQQ, "1105832910", "MXTeDJ2GwKQqrNHt",
                "https://leancloud.cn/1.1/sns/goto/imy89d1x44jf3sh5");
        SNS.loginWithCallback(this, SNSType.AVOSCloudSNSQQ, myCallback);

    }

    @Override
    protected void loadData() {

    }

    // 当登录完成后，请调用 SNS.onActivityResult(requestCode, resultCode, data, type);
    // 这样你的回调用将会被调用到
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, SNSType.AVOSCloudSNSQQ);
    }
}
