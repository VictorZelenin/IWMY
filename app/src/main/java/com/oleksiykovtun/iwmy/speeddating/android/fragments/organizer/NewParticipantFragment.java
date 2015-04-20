package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.common.ProfileEditFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class NewParticipantFragment extends ProfileEditFragment {

    private User user = null;
    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_new_participant, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_register);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_register:
                user = fillWithoutPassword(new User());
                user.generateId();
                if (checkWithoutPassword(user)) {
                    post(Api.USERS + Api.ADD, User[].class, user);
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
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.USERS + Api.ADD:
                if (!response.isEmpty()) {
                    post(Api.ATTENDANCES + Api.ADD, Attendance[].class, new Attendance(user, event));
                } else {
                    showToastLong(R.string.message_user_exists);
                }
                break;
            case Api.ATTENDANCES + Api.ADD:
                if (!response.isEmpty()) {
                    showToast(R.string.message_participant_added);
                    CoolFragmentManager.showPrevious();
                }
                break;
        }
    }

}
