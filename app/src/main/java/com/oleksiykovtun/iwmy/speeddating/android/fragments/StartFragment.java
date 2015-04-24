package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.MyEventListFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.user.EventListFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;


/**
 * Created by alx on 2015-02-04.
 */
public class StartFragment extends AppFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_login);
        registerClickListener(R.id.button_register);
        // todo social login
        //registerClickListener(R.id.button_vk);
        //registerClickListener(R.id.button_facebook);
        //registerClickListener(R.id.button_google_plus);
        registerClickListener(R.id.button_for_organizers);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Account.hasUser()) {
            if (Account.getUser().getGroup().equals(User.ORGANIZER)) {
                CoolFragmentManager.showAtBottom(new MyEventListFragment());
            } else {
                CoolFragmentManager.showAtBottom(new EventListFragment());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                CoolFragmentManager.showAtTop(new AuthorizationFragment());
                break;
            case R.id.button_register:
                CoolFragmentManager.showAtTop(new com.oleksiykovtun.iwmy.speeddating.android
                        .fragments.user.RegisteringFragment());
                break;
            case R.id.button_for_organizers:
                CoolFragmentManager.showAtTop(new com.oleksiykovtun.iwmy.speeddating.android
                        .fragments.organizer.RegisteringFragment());
                break;
        }
    }

}
