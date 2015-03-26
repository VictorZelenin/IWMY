package com.oleksiykovtun.android.cooltools;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by alx on 2015-02-25.
 */
public class CoolPagerAdapter extends PagerAdapter {

    Context context;
    int[] viewIds;

    public CoolPagerAdapter(Fragment fragment, int... viewIds) {
        this.context = fragment.getActivity();
        this.viewIds = new int[viewIds.length];
        System.arraycopy(viewIds, 0, this.viewIds, 0, viewIds.length);
    }

    @Override
    public int getCount() {
        return viewIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((Activity) context).findViewById(viewIds[position]).getTag().toString();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return ((Activity) context).findViewById(viewIds[position]);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
