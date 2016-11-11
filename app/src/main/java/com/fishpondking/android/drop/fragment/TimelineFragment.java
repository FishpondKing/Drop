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
 * Date: 2016/11/3:19:41
 * Email: song511653502@gmail.com
 * Description: “文化”选项卡中“时间轴”选项卡对应的界面
 */

public class TimelineFragment extends Fragment {

    View mView;
    TextView mTextView;

    public static TimelineFragment newInstance() {
        TimelineFragment timelineFragment = new TimelineFragment();
        return timelineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_culture_timeline, container, false);

        mTextView = (TextView) mView.findViewById(R.id.text_view_timeline);

        return mView;
    }
}
