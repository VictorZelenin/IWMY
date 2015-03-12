package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class NewEventFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_new_event, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_apply);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_apply:
                String time = CoolFormatter.getDateTime(getDateMillis(R.id.date_picker)
                        + getTimeMillis(R.id.time_picker));
                String place = getEditText(R.id.input_event_place);
                String photoBase64 = "";
                String streetAddress = getEditText(R.id.input_event_place);
                String freePlaces = getEditText(R.id.input_event_places);
                String cost = getEditText(R.id.input_event_cost);
                String description = getEditText(R.id.input_event_description);

                Event event = new Event(Account.getUser(this).getEmail(), time, place,
                        streetAddress, photoBase64, freePlaces, cost, description);
                if (checkEvent(event)) {
                    post("http://iwmy-speed-dating.appspot.com/events/add", Event[].class, event);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    private boolean checkEvent(Event event) {
        return CoolFormatter.isDateTimeValid(event.getTime())
                && event.getOrganizerEmail().contains("@");
    }

    @Override
    public void onReceiveWebData(List response) {
        if (! response.isEmpty()) {
            showToast(R.string.message_event_added);
            CoolFragmentManager.switchToPreviousFragment();
        } else {
            showToast(R.string.message_connection_error);
        }
    }

}
