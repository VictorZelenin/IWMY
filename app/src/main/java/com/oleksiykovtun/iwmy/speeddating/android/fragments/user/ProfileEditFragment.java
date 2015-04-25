package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class ProfileEditFragment extends com.oleksiykovtun.iwmy.speeddating.android.fragments.common.ProfileEditFragment {

    private User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_edit_profile, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_ok);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_settings);

        user = (User) getAttachment();
        fillForms(user);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_ok:
                User newUser = makeUser();
                if (check(newUser)) {
                    post(Api.USERS + Api.REPLACE, User[].class, user, newUser);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
            case R.id.button_settings:
                CoolFragmentManager.show(new SettingsFragment(), Account.getUser());
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            Account.saveUser(response.get(0));
            CoolFragmentManager.showAtBottom(new EventListFragment());
        } else {
            showToastLong(R.string.message_user_exists);
        }
    }
}
