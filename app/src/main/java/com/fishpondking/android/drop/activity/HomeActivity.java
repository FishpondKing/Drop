package com.fishpondking.android.drop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.fragment.CleaningFragment;
import com.fishpondking.android.drop.fragment.CultureFragment;
import com.fishpondking.android.drop.fragment.DiaryFragment;
import com.fishpondking.android.drop.fragment.LifeFragment;
import com.fishpondking.android.drop.fragment.MessageFragment;
import com.fishpondking.android.drop.fragment.MoneyFragment;
import com.fishpondking.android.drop.fragment.MoreOptionsFragment;
import com.fishpondking.android.drop.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:10:51
 * Email: song511653502@gmail.com
 * Description: 主界面
 */

public class HomeActivity extends BaseActivity
        implements BottomNavigationBar.OnTabSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private BottomNavigationBar mBottomNavigationBar;
    private DiaryFragment mDiaryFragment;
    private CultureFragment mCultureFragment;
    private LifeFragment mLifeFragment;
    private MessageFragment mMessageFragment;
    private MoreOptionsFragment mMoreOptionsFragment;

    private List<Fragment> mMainActivityFragments;


    public static void activityStart(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mDiaryFragment = DiaryFragment.newInstance();
        mCultureFragment = CultureFragment.newInstance();
        mLifeFragment = LifeFragment.newInstance();
        mMessageFragment = MessageFragment.newInstance();
        mMoreOptionsFragment = MoreOptionsFragment.newInstance();
        mMainActivityFragments = getMainActivityFragments();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.activity_main_fragment_container);

        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment_container, mDiaryFragment)
                    .add(R.id.activity_main_fragment_container, mCultureFragment)
                    .add(R.id.activity_main_fragment_container, mLifeFragment)
                    .add(R.id.activity_main_fragment_container, mMessageFragment)
                    .add(R.id.activity_main_fragment_container, mMoreOptionsFragment)
                    .hide(mCultureFragment)
                    .hide(mLifeFragment)
                    .hide(mMessageFragment)
                    .hide(mMoreOptionsFragment)
                    .commit();
        }

        mBottomNavigationBar =
                (BottomNavigationBar) findViewById(R.id.activity_main_bottom_navigation_bar);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_bottom_bar_diary,
                        R.string.bottom_bar_diary))
                .addItem(new BottomNavigationItem(R.drawable.ic_bottom_bar_culture,
                        R.string.bottom_bar_culture))
                .addItem(new BottomNavigationItem(R.drawable.ic_bottom_bar_life,
                        R.string.bottom_bar_life))
                .addItem(new BottomNavigationItem(R.drawable.ic_bottom_bar_message_board,
                        R.string.bottom_bar_message))
                .addItem(new BottomNavigationItem(R.drawable.ic_bottom_bar_more,
                        R.string.bottom_bar_more_options))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);

    }

    @Override
    protected void loadData() {

    }

    private List<Fragment> getMainActivityFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(mDiaryFragment);
        fragments.add(mCultureFragment);
        fragments.add(mLifeFragment);
        fragments.add(mMessageFragment);
        fragments.add(mMoreOptionsFragment);
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (mMainActivityFragments != null) {
            if (position < mMainActivityFragments.size()) {
                mFragment = mMainActivityFragments.get(position);
                mFragmentManager.beginTransaction()
                        .show(mFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onTabUnselected(int position) {
        if (mMainActivityFragments != null) {
            if (position < mMainActivityFragments.size()) {
                mFragment = mMainActivityFragments.get(position);
                mFragmentManager.beginTransaction()
                        .hide(mFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }
}
