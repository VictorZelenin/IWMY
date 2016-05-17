package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment;

public class RegistrationFragment extends ProfileEditFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
        }
    }
}
