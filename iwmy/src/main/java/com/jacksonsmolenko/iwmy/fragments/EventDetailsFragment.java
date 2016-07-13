package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.EventDetailsAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 13.07.16.
 */
public class EventDetailsFragment extends AppFragment {
    private List<Event> eventList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        registerContainerView(view);

        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);

        eventList = new ArrayList<>();
        // testing move
        eventList.add(null);
        eventList.add(null);
        eventList.add(null);
        eventList.add(null);
        eventList.add(null);
        eventList.add(null);

        EventDetailsAdapter adapter = new EventDetailsAdapter(eventList);

//        post(Api.EVENTS + Api.GET_ALL_FOR_USER, Event[].class, Account.getUser());

        RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.event_details_holder);

//        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        registerItemClickListener(eventRecyclerAdapter);
        eventRecyclerView.setAdapter(adapter);

        return view;
    }

}
