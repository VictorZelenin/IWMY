package com.jacksonsmolenko.iwmy.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jacksonsmolenko.iwmy.fragments.TutorialPageFragment;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    public TutorialPagerAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        return TutorialPageFragment.newInstance(position);
    }
}
