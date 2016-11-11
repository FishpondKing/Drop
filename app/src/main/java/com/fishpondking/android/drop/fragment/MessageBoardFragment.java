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
 * Date: 2016/11/5:16:24
 * Email: song511653502@gmail.com
 * Description: “消息”选项卡中“留言板”选项卡对应的界面
 */

public class MessageBoardFragment extends Fragment{

    View mView;
    TextView mTextView;

    public static MessageBoardFragment newInstance(){
        MessageBoardFragment messageBoardFragment = new MessageBoardFragment();
        return messageBoardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message_board, container, false);

        mTextView = (TextView)mView.findViewById(R.id.text_view_message_board);

        return mView;
    }
}
