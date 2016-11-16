package com.fishpondking.android.drop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.fragment.LoginFragment;
import com.fishpondking.android.drop.fragment.SigninFragment;
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
        super(context, fragmentManager, R.array.tab_layout_login_and_signin);
    }

    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(LoginFragment.newInstance());
        fragments.add(SigninFragment.newInstance());
        return fragments;
    }
}
