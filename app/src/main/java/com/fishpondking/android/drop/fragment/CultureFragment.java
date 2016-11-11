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
import com.fishpondking.android.drop.adapter.CulturePagerAdapter;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:17:11
 * Email: song511653502@gmail.com
 * Description: 主界面中“文化”选项卡对应的界面
 */

public class CultureFragment extends Fragment {

    private static final String TAG = CultureFragment.class.getSimpleName();

    private View mView;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static CultureFragment newInstance() {
        CultureFragment cultureFragment = new CultureFragment();
        return cultureFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_culture, container, false);

        mFragmentManager = getChildFragmentManager();
        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager_culture);
        mViewPager.setAdapter(new CulturePagerAdapter(getActivity(), mFragmentManager));

        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout_culture);
        mTabLayout.setupWithViewPager(mViewPager);

        return mView;
    }
}
