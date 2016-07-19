package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.adapters.EventDetailsRecycleAdapter;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 13.07.16.
 */

public class EventDetailsFragment extends AppFragment {

    private List<Event> eventList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        registerContainerView(view);

        RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.event_details_holder);

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        debugFillUpList();
        eventRecyclerView.setAdapter(new EventDetailsRecycleAdapter(eventList));
        return view;
    }

    private void debugFillUpList() {
        for (int i = 0; i < 5; i++) {
            eventList.add(null);
        }
    }
}
