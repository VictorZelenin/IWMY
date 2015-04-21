package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.StartFragment;


/**
 * Created by alx on 2015-02-04.
 */
public class SettingsFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_logout);
        registerClickListener(R.id.button_edit_my_profile);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                Account.removeUser(this);
                CoolFragmentManager.showAtBottom(new StartFragment());
                break;
            case R.id.button_edit_my_profile:
                CoolFragmentManager.show(new ProfileEditFragment(), Account.getUser(this));
                break;
        }
    }

}
