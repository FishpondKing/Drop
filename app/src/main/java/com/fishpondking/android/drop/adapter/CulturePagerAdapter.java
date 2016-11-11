package com.fishpondking.android.drop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.fragment.DormitoryActivitiesFragment;
import com.fishpondking.android.drop.fragment.DormitoryInformationFragment;
import com.fishpondking.android.drop.fragment.TimelineFragment;
import com.fishpondking.android.drop.utils.TabLayoutPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/4:9:24
 * Email: song511653502@gmail.com
 * Description: CultureFragment中ViewPager的适配器
 */

public class CulturePagerAdapter extends TabLayoutPagerAdapter {

    public CulturePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager, R.array.tab_layout_culture);
    }

    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(DormitoryInformationFragment.newInstance());
        fragments.add(DormitoryActivitiesFragment.newInstance());
        fragments.add(TimelineFragment.newInstance());
        return fragments;
    }

}
