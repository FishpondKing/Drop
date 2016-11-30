package com.fishpondking.android.drop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.fragment.CleaningFragment;
import com.fishpondking.android.drop.fragment.MoneyFragment;
import com.fishpondking.android.drop.utils.TabLayoutPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:9:22
 * Email: song511653502@gmail.com
 * Description:
 */

public class LifePagerAdapter extends TabLayoutPagerAdapter {

    public LifePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager, R.array.tab_layout_life);
    }

    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(MoneyFragment.newInstance());
        fragments.add(CleaningFragment.newInstance());
        return fragments;
    }
}
