package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;

public class RegistrationFragment extends ProfileEditFragment {

    String password1;
    String password2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_register);
        setText(R.id.button_register, R.string.profile_button_register);
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
}
