package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
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
public class MyEventListFragment extends CoolFragment {

    private List<Event> currentEventList = new ArrayList<Event>();
    private List<Event> pastEventList = new ArrayList<Event>();

    private EventRecyclerAdapter currentEventRecyclerAdapter
            = new EventRecyclerAdapter(currentEventList);
    private EventRecyclerAdapter pastEventRecyclerAdapter
            = new EventRecyclerAdapter(pastEventList);

    @Override
    public void onStart() {
        super.onStart();
        Event eventWildcard = new Event(Account.getUser(this).getEmail(), "", "", "", "", "",
                "", "");
        post(Api.EVENTS + Api.GET, Event[].class, eventWildcard);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_my_event_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_add_event);
        registerClickListener(R.id.button_settings);

        RecyclerView currentEventRecyclerView = (RecyclerView) view
                .findViewById(R.id.event_list_holder_current);
        currentEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(currentEventRecyclerAdapter);
        currentEventRecyclerView.setAdapter(currentEventRecyclerAdapter);

        RecyclerView pastEventRecyclerView = (RecyclerView) view
                .findViewById(R.id.event_list_holder_past);
        pastEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(pastEventRecyclerAdapter);
        pastEventRecyclerView.setAdapter(pastEventRecyclerAdapter);

        ViewPager pager = (ViewPager) view.findViewById(R.id.events_pager);
        pager.setAdapter(new CoolPagerAdapter(this,
                R.id.page_events_current, R.id.page_events_past));

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        currentEventList.clear();
        pastEventList.clear();
        for (Event event : (List<Event>) response) {
            if (CoolFormatter.isDateTimeFuture(event.getTime())) {
                currentEventList.add(event);
            } else {
                pastEventList.add(event);
            }
        }
        currentEventRecyclerAdapter.notifyDataSetChanged();
        pastEventRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_event:
                CoolFragmentManager.switchToFragment(new NewEventFragment());
                break;
            case R.id.button_settings:
                CoolFragmentManager.switchToFragment(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.switchToFragment(new EventFragment(), objectAtClicked);
    }

    @Override
    public void onLongClick(Serializable objectAtClicked) {
        final Event event = (Event) objectAtClicked;
        new AlertDialog.Builder(getActivity()).setTitle(R.string.label_delete)
                .setMessage(getText(R.string.label_delete_this_event) + event.getPlace() +
                        " " + event.getStreetAddress())
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        post(Api.EVENTS + Api.DELETE, Event[].class, event);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                })
                .create().show();
    }

}
