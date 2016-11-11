package com.fishpondking.android.drop.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fishpondking.android.drop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/10:17:04
 * Email: song511653502@gmail.com
 * Description:
 */

public class WelcomeGuidePagerAdapter extends PagerAdapter {

    private List<View> mViews;

    public WelcomeGuidePagerAdapter(List<View> views){
        super();
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(mViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(mViews.get(position));
        return mViews.get(position);
    }
}
