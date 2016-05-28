package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;

public class UserProfileEditFragment extends ProfileEditFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);

        fillForms(Account.getUser());

        return view;
    }
}
