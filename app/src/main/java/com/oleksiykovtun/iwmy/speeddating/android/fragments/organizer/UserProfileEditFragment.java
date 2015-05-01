package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class UserProfileEditFragment extends ProfileEditFragment {

    private User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_edit_participant, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_ok);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_photo);
        registerClickListener(R.id.button_settings);

        user = (User) getAttachment();
        fillForms(user);

        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_ok:
                User newUser = makeUserWithoutPassword();
                if (checkWithoutPassword(newUser)) {
                    post(Api.USERS + Api.REPLACE, User[].class, user, newUser);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            CoolFragmentManager.showPrevious();
        } else {
            showToastLong(R.string.message_user_exists);
        }
    }
}
