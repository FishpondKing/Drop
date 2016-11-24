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
 * Date: 2016/11/3:17:15
 * Email: song511653502@gmail.com
 * Description: 主界面中“更多”选项卡对应的界面
 */

public class MoreOptionsFragment extends Fragment{

    private View mView;
    private TextView mTextView;

    public static MoreOptionsFragment newInstance(){
        MoreOptionsFragment moreOptionsFragment = new MoreOptionsFragment();
        return moreOptionsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more_options, container, false);

        return mView;
    }
}
