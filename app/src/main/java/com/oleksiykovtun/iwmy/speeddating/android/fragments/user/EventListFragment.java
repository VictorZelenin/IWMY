package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.EventRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class EventListFragment extends AppFragment {

    private List<Event> eventList = new ArrayList<Event>();
    private EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(eventList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_options);
        registerClickListener(R.id.button_settings);

        post(Api.EVENTS + Api.GET_ALL, Event[].class);

        RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.event_list_holder);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerItemClickListener(eventRecyclerAdapter);
        eventRecyclerView.setAdapter(eventRecyclerAdapter);

        return view;
    }

    @Override
    public void onPostReceive(List response) {
        eventList.clear();
        eventList.addAll(response);
        eventRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.showAtTop(new EventAttendFragment(), objectAtClicked);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_options:
                openMenu(view);
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_my_events:
                CoolFragmentManager.show(new MyEventListFragment(), null);
                break;
            case R.id.menu_all_events:
                CoolFragmentManager.show(new EventListFragment(), null);
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.user, menu);
    }

}
