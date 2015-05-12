package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.MyEventListFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.user.EventListFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;


/**
 * Created by alx on 2015-02-04.
 */
public class AuthorizationFragment extends AppFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_enter);
        registerClickListener(R.id.button_forgot_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_enter:
                String usernameOrEmail = getEditText(R.id.input_username);
                String password = getEditText(R.id.input_password);
                if (!usernameOrEmail.isEmpty() && !password.isEmpty()) {
                    // to do security
                    User wildcardLoginUser = new User(usernameOrEmail, password, usernameOrEmail,
                            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                    post(Api.USERS + Api.GET_LOGIN, User[].class, wildcardLoginUser);
                } else {
                    showToastLong(R.string.message_no_user_wrong_password);
                }
                break;
            case R.id.button_forgot_password:
                post(Api.MAIL + Api.RESET_PASSWORD, Email[].class,
                        getPasswordResetEmail(getEditText(R.id.input_username)));
                getViewById(R.id.button_forgot_password).setEnabled(false);
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
            case Api.USERS + Api.GET_LOGIN:
                if (response.size() != 1) {
                    showToastLong(R.string.message_no_user_wrong_password);
                } else {
                    Account.saveUser(response.get(0));
                    if (Account.getUser().getGroup().equals(User.ORGANIZER)) {
                        CoolFragmentManager.showAtBottom(new MyEventListFragment());
                    } else {
                        CoolFragmentManager.showAtBottom(new EventListFragment());
                    }
                }
                break;
            case Api.MAIL + Api.RESET_PASSWORD:
                if (response.size() == 1) {
                    showToastLong(R.string.message_password_reset_sent);
                } else {
                    showToastLong(R.string.message_password_reset_suspicious);
                    setText(R.id.label_support, Api.APP_SUPPORT_EMAIL);
                }
                break;
        }
    }

}
