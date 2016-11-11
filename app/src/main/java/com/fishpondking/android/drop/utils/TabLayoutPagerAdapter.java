package com.fishpondking.android.drop.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/5:20:36
 * Email: song511653502@gmail.com
 * Description: 支持XMLStringArray用于TabLayout的ViewPager适配器
 */

public abstract class TabLayoutPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mMessageFragments;
    private Resources mResources;
    private String[] fragmentTitles;

    /**
     * Method: getFragments()
     * Description: 为Adapter添加所包含的Fragments
     * Param:
     * Return: List<Fragment> 包含的Fragment的List
     * Author: FishpondKing
     * Date: 2016/11/10:17:11
     */

    protected abstract List<Fragment> getFragments();

    /**
     * Method: TabLayoutPagerAdapter(Context context, FragmentManager fragmentManager,
     *         int XMLStringArray)
     * Description: 构造函数
     * Param: context Fragment所在的Context（Activity） fragmentManager 当前的FragmentManager
     *        XMLStringArray R.array.XXX
     * Return:
     * Author: FishpondKing
     * Date: 2016/11/5:21:34
     */

    public TabLayoutPagerAdapter(Context context, FragmentManager fragmentManager,
                                 int XMLStringArray) {
        super(fragmentManager);
        mResources = context.getResources();
        fragmentTitles = mResources.getStringArray(XMLStringArray);
        mMessageFragments = getFragments();
    }

    @Override
    public Fragment getItem(int position) {
        return mMessageFragments.get(position);
    }

    @Override
    public int getCount() {
        return mMessageFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }
}
