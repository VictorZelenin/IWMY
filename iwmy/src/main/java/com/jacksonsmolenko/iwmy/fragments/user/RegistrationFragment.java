package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;
import com.jacksonsmolenko.iwmy.fragments.user.EventListFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

public class RegistrationFragment extends ProfileEditFragment {

    private String password1;
    private String password2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_register);
        password1 = getEditText(R.id.input_password);
        password2 = getEditText(R.id.input_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_register:
                User user = makeUser();

                if (!password1.equals(password2)) {
                    showToast("wrong password");
                } else {
                    if (check(user)) {
                        post(Api.USERS + Api.ADD, User[].class, user);
                    } else {
                        showToast(R.string.message_inputs_error);
                    }
                }
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            Account.saveUser(response.get(0));
            showToast(R.string.message_registered);
            CoolFragmentManager.showAtBottom(new LoginFragment());
        } else {
            showToastLong(R.string.message_user_exists);
        }
    }
}
