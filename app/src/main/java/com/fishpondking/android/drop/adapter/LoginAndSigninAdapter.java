package com.fishpondking.android.drop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.utils.TabLayoutPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/15:10:23
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginAndSigninAdapter extends TabLayoutPagerAdapter {
    public LoginAndSigninAdapter(Context context, FragmentManager fragmentManager) {
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
