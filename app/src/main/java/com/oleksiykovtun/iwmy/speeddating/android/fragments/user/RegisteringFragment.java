package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class RegisteringFragment extends ProfileEditFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registering, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_register);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_photo);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_register:
                User user = makeUser();
                if (check(user)) {
                    post(Api.USERS + Api.ADD, User[].class, user);
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
            showToast(R.string.message_registered);
            CoolFragmentManager.showAtBottom(new EventListFragment());
        } else {
            showToastLong(R.string.message_user_exists);
        }
    }

}
