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
 * Date: 2016/11/3:17:13
 * Email: song511653502@gmail.com
 * Description: 主界面中“财务”选项卡中对应的界面
 */

public class MoneyFragment extends Fragment {

    private View mView;
    private TextView mTextView;

    public static MoneyFragment newInstance() {
        MoneyFragment moneyFragment = new MoneyFragment();
        return moneyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_money, container, false);

        mTextView = (TextView) mView.findViewById(R.id.text_view_money);

        return mView;

    }
}
