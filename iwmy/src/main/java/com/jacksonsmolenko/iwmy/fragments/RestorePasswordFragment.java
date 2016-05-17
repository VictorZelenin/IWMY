package com.jacksonsmolenko.iwmy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

public class RestorePasswordFragment extends AppFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restore_password, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_reset_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_reset_password:
                post(Api.MAIL + Api.RESET_PASSWORD, Email[].class,
                        getPasswordResetEmail(getEditText(R.id.input_username)));
                break;
        }
    }

    private Email getPasswordResetEmail(String usernameOrEmail) {
        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name), usernameOrEmail,
                usernameOrEmail, "" + getText(R.string.mail_subject_password_reset),
                ("" + getText(R.string.mail_text_password_reset))
                        .replace("CONTACTS_SPEED_DATING", Api.APP_SUPPORT_EMAIL));
    }

    @Override
    public void onPostReceive(String tag, List response) {
        switch (tag) {
            case Api.MAIL + Api.RESET_PASSWORD:
                if (response.size() == 1) {
                    showToastLong(R.string.message_password_reset_sent);
                } else {
                    showToastLong(R.string.message_password_reset_suspicious);
                    /*setText(R.id.label_support, Api.APP_SUPPORT_EMAIL);*/
                }
                break;
        }
    }
}
