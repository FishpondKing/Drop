package com.fishpondking.android.drop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fishpondking.android.drop.R;

/**
 * Author: FishpondKing
 * Date: 2016/11/5:16:23
 * Email: song511653502@gmail.com
 * Description: “消息”选项卡中“通知”选项卡对应的界面
 */

public class NotificationFragment extends Fragment {

    private View mView;
    private TextView mTextView;

    public static NotificationFragment newInstance(){
        NotificationFragment notificationFragment = new NotificationFragment();
        return notificationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message_notification, container, false);

        mTextView = (TextView)mView.findViewById(R.id.text_view_message_notification);

        return mView;
    }
}
