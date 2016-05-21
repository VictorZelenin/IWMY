package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.TutorialPagerAdapter;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.jacksonsmolenko.iwmy.fragments.user.LoginFragment;


public class TutorialFragment extends AppFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        registerContainerView(view);
        Button bt = (Button) view.findViewById(R.id.start);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoolFragmentManager.showAtBottom(new LoginFragment());
            }
        });

        ViewPager pager=(ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(new TutorialPagerAdapter(getFragmentManager()));
        return view;
    }
}
