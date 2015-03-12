package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.EventRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventListFragment extends CoolFragment {

    private List<Event> eventList = new ArrayList<Event>();
    private EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(eventList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        post("http://iwmy-speed-dating.appspot.com/events/get/all", Event[].class);

        RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.event_list_holder);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerItemClickListener(eventRecyclerAdapter);
        eventRecyclerView.setAdapter(eventRecyclerAdapter);

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        eventList.clear();
        eventList.addAll(response);
        eventRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.switchToFragment(new EventAttendFragment(), (Event)objectAtClicked);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_settings:
                CoolFragmentManager.switchToFragment(new SettingsFragment());
                break;
        }
    }

}
