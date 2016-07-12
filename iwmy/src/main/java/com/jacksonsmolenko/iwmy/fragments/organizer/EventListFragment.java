package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.EventRecyclerAdapter;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.Time;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends AppFragment{

    private List<Event> eventList = new ArrayList<Event>();
    private EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(eventList, true);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_list, container, false);
        registerContainerView(view);

        post(Api.EVENTS + Api.GET_ALL_FOR_USER, Event[].class, Account.getUser());

        RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.event_list_holder);

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerItemClickListener(eventRecyclerAdapter);
        eventRecyclerView.setAdapter(eventRecyclerAdapter);

        return view;
    }

    @Override
    public void onPostReceive(List response) {
        // filtering by expiration time on device to ensure relativity to device time
        final long eventExpirationTimeMillis = 3600000;
        eventList.clear();
        for (Event event : (List<Event>)response) {
            if (Time.getMillisFromDateTime(event.getTime()) < eventExpirationTimeMillis) {
                eventList.add(event);
            }
        }
        eventRecyclerAdapter.notifyDataSetChanged();
    }
}
