package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventAttendFragment extends CoolFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_attend, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_attend);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        ImageManager.setEventPic(getImageView(R.id.image_event_pic), event);
        setText(R.id.label_organizer, event.getOrganizerEmail());
        setText(R.id.label_event_address,
                R.string.label_event_address, event.getStreetAddress());
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
    public void onStart() {
        super.onStart();
        post(Api.ATTENDANCES + Api.GET, Attendance[].class,
                new Attendance(Account.getUser(this), event));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_attend:
                post(Api.ATTENDANCES + Api.ADD, Attendance[].class,
                        new Attendance(Account.getUser(this), event));
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onReceiveWebData(List response) {
        if (!response.isEmpty()) {
            CoolFragmentManager.show(new EventStartFragment(), event);
        }
    }

}
