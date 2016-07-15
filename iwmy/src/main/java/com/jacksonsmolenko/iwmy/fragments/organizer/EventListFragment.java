package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.EventRecyclerAdapter;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.jacksonsmolenko.iwmy.fragments.user.EventDetailsFragment;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.Time;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends AppFragment {

    private List<Event> actualEventList = new ArrayList<Event>();

    private EventRecyclerAdapter actualEventRecyclerAdapter
            = new EventRecyclerAdapter(actualEventList);

    @Override
    public void onResume() {
        super.onResume();
        requestEventList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_event_list, container, false);
        registerContainerView(view);

        RecyclerView actualEventRecyclerView = (RecyclerView) view
                .findViewById(R.id.event_list_holder);
        actualEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(actualEventRecyclerAdapter);
        registerClickListener(R.id.fab);
        registerClickListener(R.id.details_button);
        actualEventRecyclerView.setAdapter(actualEventRecyclerAdapter);


        return view;
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.EVENTS + Api.GET:
                actualEventList.clear();
                for (Event event : (List<Event>) response) {
                    actualEventList.add(event);
                }
                actualEventRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET:

                break;
            case Api.EVENTS + Api.DELETE:
                requestEventList();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.fab:
                CoolFragmentManager.showAtTop(new NewEventEditFragment());
                break;
//            case R.id.details_button:
//                CoolFragmentManager.showAtTop(new EventDetailsFragment());
//                break;
        }
    }

    private void requestEventList() {
        Event eventWildcard = new Event(Account.getUser().getEmail(), "", "", "", "", "", "",
                "", "", "", "", "", "");
        post(Api.EVENTS + Api.GET, Event[].class, eventWildcard);
    }
}
