package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.MyEventListFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.user.EventListFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;


/**
 * Created by alx on 2015-02-04.
 */
public class AuthorizationFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_enter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_enter:
                String userId = getEditText(R.id.input_username);
                String password = getEditText(R.id.input_password);
                if (!userId.isEmpty() && !password.isEmpty()) {
                    // to do security
                    User wildcardLoginUser = new User(userId, password, userId,
                            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                    post(Api.USERS + Api.GET_LOGIN, User[].class, wildcardLoginUser);
                } else {
                    showToastLong(R.string.message_no_user_wrong_password);
                }

        }
    }

    @Override
    public void onReceiveWebData(List response) {
        if (response.size() != 1) {
            showToastLong(R.string.message_no_user_wrong_password);
        } else {
            Account.saveUser(this, response.get(0));
            if (Account.getUser(this).getGroup().equals(User.ORGANIZER)) {
                CoolFragmentManager.showAtBottom(new MyEventListFragment());
            } else {
                CoolFragmentManager.showAtBottom(new EventListFragment());
            }
        }
    }

}
