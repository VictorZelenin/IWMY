package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.MyEventListFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.user.EventListFragment;


/**
 * Created by alx on 2015-02-04.
 */
public class StartFragment extends CoolFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_login);
        registerClickListener(R.id.button_register);
        registerClickListener(R.id.button_vk);
        registerClickListener(R.id.button_facebook);
        registerClickListener(R.id.button_google_plus);
        registerClickListener(R.id.button_for_organizers);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Account.hasUser(this)) {
            if (Account.getUser(this).getGroup().equals("organizer")) {
                CoolFragmentManager.switchToRootFragment(new MyEventListFragment());
            } else {
                CoolFragmentManager.switchToRootFragment(new EventListFragment());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                CoolFragmentManager.switchToFragment(new AuthorizationFragment());
                break;
            case R.id.button_register:
                CoolFragmentManager.switchToFragment(new com.oleksiykovtun.iwmy.speeddating.android
                        .fragments.user.RegisteringFragment());
                break;
            case R.id.button_for_organizers:
                CoolFragmentManager.switchToFragment(new com.oleksiykovtun.iwmy.speeddating.android
                        .fragments.organizer.RegisteringFragment());
                break;
        }
    }

}
