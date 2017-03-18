package com.fishpondking.android.drop.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.PostDormitoryActivityActivity;
import com.github.clans.fab.FloatingActionButton;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:17:14
 * Email: song511653502@gmail.com
 * Description: 主界面中“活动”选项卡对应的界面
 */

public class DormitoryActivityFragment extends Fragment {

    public static final int DORMITORY_ACTIVITY_LIST_SIZE = 41;
    public static final int ONE_DORMITORY_DOWNLOAD_SUCCESS = 42;
    public static final int REQUEST_ADD_DORMITORY_ACTIVITY = 43;
    public static final int REQUEST_REFRESH_DORMITORY_ACTVITY_LIST = 44;
    public static final int EACH_PAGE_DORMITORY_ACTIVITY_COUNT = 1000;

    private View mView;
    private Activity mActivity;
    private Fragment mFragment;
    private FloatingActionButton mFloatingActionButtonPostActivity;

    public static DormitoryActivityFragment newInstance(){
        DormitoryActivityFragment dormitoryActivityFragment = new DormitoryActivityFragment();
        return dormitoryActivityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_activity, container, false);
        mActivity = getActivity();
        mFragment = DormitoryActivityFragment.this;


        //发送活动
        mFloatingActionButtonPostActivity = (FloatingActionButton) mView.findViewById(R.id.floating_action_button_post_activity);
        mFloatingActionButtonPostActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDormitoryActivityActivity.activityStart(getActivity(),mFragment);
            }
        });

        return mView;
    }
}
