package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;


/**
 * Created by alx on 2015-02-04.
 */
public class SettingsFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_logout);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                Account.removeUser(this);
                CoolFragmentManager.switchToRootFragment(new StartFragment());
        }
    }

}
