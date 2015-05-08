package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventAttendFragment extends AppFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_attend, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_attend);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        if (!event.getActual().equals("true")) {
            setText(R.id.button_attend, "" + getText(R.string.label_event_is_old));
            setButtonEnabled(R.id.button_attend, false);
        }

        ImageManager.setEventPhoto(getImageView(R.id.photo), event.getPhoto());
        setText(R.id.label_organizer, event.getPlace());
        setText(R.id.label_event_address,
                R.string.label_event_address, event.getFullStreetAddress());
        setText(R.id.label_event_cost,
                R.string.label_event_cost, event.getCost());
        setText(R.id.label_event_description,
                R.string.label_event_description, event.getDescription());
        setText(R.id.label_event_organizer_and_place,
                R.string.label_event_organizer_and_place,
                event.getOrganizerEmail() + ", " + event.getPlace());
        setText(R.id.label_event_time,
                R.string.label_event_time, event.getTime());
        setText(R.id.label_event_places,
                R.string.label_event_places, event.getFreePlaces());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        post(Api.ATTENDANCES + Api.GET, Attendance[].class,
                new Attendance(Account.getUser(), event));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_attend:
                post(Api.ATTENDANCES + Api.PUT, Attendance[].class,
                        new Attendance(Account.getUser(), event));
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            if (event.getActual().equals("false")) {
                CoolFragmentManager.show(new CoupleUserListFragment(), event);
            } else {
                CoolFragmentManager.show(new EventStartFragment(), event);
            }
        }
    }

}
