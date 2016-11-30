package com.fishpondking.android.drop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.adapter.LifePagerAdapter;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:9:36
 * Email: song511653502@gmail.com
 * Description:
 */

public class LifeFragment extends Fragment{

    private static final String TAG = LifeFragment.class.getSimpleName();

    private View mView;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static LifeFragment newInstance() {
        LifeFragment lifeFragment = new LifeFragment();
        return lifeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_life, container, false);

        mFragmentManager = getChildFragmentManager();
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager_life);
        mViewPager.setAdapter(new LifePagerAdapter(getActivity(), mFragmentManager));

        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout_life);
        mTabLayout.setupWithViewPager(mViewPager);

        return mView;
    }
}
