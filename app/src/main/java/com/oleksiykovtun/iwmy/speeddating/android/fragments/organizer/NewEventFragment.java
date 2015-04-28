package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class NewEventFragment extends AppFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_new_event, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_select_time);
        registerClickListener(R.id.button_apply);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_select_time:
                openTimePicker();
                break;
            case R.id.button_apply:
                String time = getLabelText(R.id.label_event_date) + " "
                        + getLabelText(R.id.label_event_time);
                String place = getEditText(R.id.input_event_place);
                String photoBase64 = "";
                String streetAddress = getEditText(R.id.input_event_address);
                String freePlaces = getEditText(R.id.input_event_places);
                String cost = getEditText(R.id.input_event_cost);
                String description = getEditText(R.id.input_event_description);

                Event event = new Event(Account.getUser().getEmail(), time, place,
                        streetAddress, photoBase64, freePlaces, cost, description);
                if (checkEvent(event)) {
                    post(Api.EVENTS + Api.ADD, Event[].class, event);
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
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            showToast(R.string.message_event_added);
            CoolFragmentManager.showPrevious();
        } else {
            showToastLong(R.string.message_event_exists);
        }
    }

    @Override
    public void onDateSet(String dateString) {
        setText(R.id.label_event_date, dateString);
    }

    @Override
    public void onTimeSet(String timeString) {
        setText(R.id.label_event_time, timeString);
    }

}
