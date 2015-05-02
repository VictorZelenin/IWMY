package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventEditFragment
        extends com.oleksiykovtun.iwmy.speeddating.android.fragments.common.EventEditFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_edit_event, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_select_time);
        registerClickListener(R.id.button_photo);
        registerClickListener(R.id.button_apply);

        event = (Event) getAttachment();
        fillForms(event);

        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_apply:
                Event newEvent = makeEvent();
                if (checkEvent(newEvent)) {
                    post(Api.EVENTS + Api.REPLACE, Event[].class, event, newEvent);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    @Override
    public void onPostReceive(List response) {
        if (!response.isEmpty()) {
            CoolFragmentManager.showPrevious();
        } else {
            showToastLong(R.string.message_event_exists);
        }
    }

}
