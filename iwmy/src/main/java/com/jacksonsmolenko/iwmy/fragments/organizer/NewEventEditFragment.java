package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.common.EventEditFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

public class NewEventEditFragment extends EventEditFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_new_event, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_select_time);
        /*registerClickListener(R.id.button_photo);*/
        registerClickListener(R.id.button_apply);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_apply:
                Event event = makeEvent();
                if (checkEvent(event)) {
                    post(Api.EVENTS + Api.ADD, Event[].class, event);
                }
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            showToast(R.string.message_event_added);
            CoolFragmentManager.showPrevious();
        } else {
            showToastLong("Event exist");
        }
    }
}
