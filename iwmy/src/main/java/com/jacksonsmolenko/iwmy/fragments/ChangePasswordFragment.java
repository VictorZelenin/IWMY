package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;

public class ChangePasswordFragment extends ProfileEditFragment {

    User user = null;
    String password1 = null;
    String password2 = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_save_password);

        password1 = getEditText(R.id.input_password);
        password2 = getEditText(R.id.input_password_repeat);

        user = Account.getUser();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save_password:

                if (checkPassword(password1, password2)){
                    User newUser = changePassword(user, password1);
                    post(Api.USERS + Api.REPLACE, User[].class, user, newUser);
                } else {
                    showToast("пароли не совпадают");
                }
        }
    }
}
