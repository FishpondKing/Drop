package com.fishpondking.android.drop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.fragment.MessageBoardFragment;
import com.fishpondking.android.drop.fragment.NotificationFragment;
import com.fishpondking.android.drop.utils.TabLayoutPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/5:16:09
 * Email: song511653502@gmail.com
 * Description: MessageFragment中ViewPager的适配器
 */

public class MessagePagerAdapter extends TabLayoutPagerAdapter {

    public MessagePagerAdapter(Context context, FragmentManager fragmentManager){
        super(context,fragmentManager, R.array.tab_layout_message);
    }

    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(NotificationFragment.newInstance());
        fragments.add(MessageBoardFragment.newInstance());
        return fragments;
    }
}
