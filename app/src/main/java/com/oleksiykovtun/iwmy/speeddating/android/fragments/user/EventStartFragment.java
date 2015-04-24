package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventStartFragment extends AppFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_start, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_start);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        ImageManager.setEventPic(getImageView(R.id.image_event_pic), event);
        setText(R.id.label_organizer, event.getPlace());
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

        setText(R.id.button_start, "" + getText(R.string.label_waiting_for_organizer));
        setButtonEnabled(R.id.button_start, false);

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.EVENTS + Api.GET_FOR_TIME:
                if (! response.isEmpty()) {
                    Event pendingEvent = ((List<Event>) response).get(0);
                    event = pendingEvent; // new event has "unlocked" voting
                    String pendingEventMaxRatings = pendingEvent.getMaxRatingsPerUser();
                    if (! pendingEventMaxRatings.equals("0")
                            && ! pendingEventMaxRatings.equals("null")) {
                        post(Api.EVENTS + Api.PUT, Event[].class, pendingEvent);
                    }
                }
                break;
            case Api.EVENTS + Api.PUT:
                setText(R.id.button_start, "" + getText(R.string.button_start));
                setButtonEnabled(R.id.button_start, true);
                break;
        }

    }

    @Override
    protected void onTimerTick() {
        // checking until this event is started by organizer
        post(Api.EVENTS + Api.GET_FOR_TIME, Event[].class, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                CoolFragmentManager.showAtTop(new QuestionnaireFragment(), event);
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

}
