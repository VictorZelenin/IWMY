package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

public class UserProfileEditFragment extends ProfileEditFragment {

    private User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_register);

        user = Account.getUser();
        fillForms(user);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.button_register:
                User newUser = makeUser();
                if (check(newUser)) {
                    post(Api.USERS + Api.REPLACE, User[].class, user, newUser);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            Account.saveUser(response.get(0));
            CoolFragmentManager.showAtBottom(new EventListFragment());
        } else {
            // todo when work else?
            showToastLong("user exist" + response);
        }
    }
}
