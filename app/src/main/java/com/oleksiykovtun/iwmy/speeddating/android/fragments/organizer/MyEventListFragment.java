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
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class MyEventListFragment extends AppFragment {

    private List<Event> actualEventList = new ArrayList<Event>();
    private List<Event> pastEventList = new ArrayList<Event>();

    private Event selectedEvent = null;

    private EventRecyclerAdapter actualEventRecyclerAdapter
            = new EventRecyclerAdapter(actualEventList);
    private EventRecyclerAdapter pastEventRecyclerAdapter
            = new EventRecyclerAdapter(pastEventList);

    @Override
    public void onResume() {
        super.onResume();
        requestEventList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_my_event_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_add_event);
        registerClickListener(R.id.button_settings);

        RecyclerView actualEventRecyclerView = (RecyclerView) view
                .findViewById(R.id.event_list_holder_current);
        actualEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(actualEventRecyclerAdapter);
        actualEventRecyclerView.setAdapter(actualEventRecyclerAdapter);

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
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.EVENTS + Api.GET:
                actualEventList.clear();
                pastEventList.clear();
                for (Event event : (List<Event>) response) {
                    if (! event.getActual().equals("false")) {
                        actualEventList.add(event);
                    } else {
                        pastEventList.add(event);
                    }
                }
                actualEventRecyclerAdapter.notifyDataSetChanged();
                pastEventRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET:
                CoolFragmentManager.showAtTop(new EventFragment(), selectedEvent);
                break;
            case Api.EVENTS + Api.DELETE:
                requestEventList();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_event:
                CoolFragmentManager.showAtTop(new NewEventFragment());
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        selectedEvent = (Event)objectAtClicked;
        if (selectedEvent.getActual().equals("false")) {
            CoolFragmentManager.showAtTop(new CoupleListFragment(), selectedEvent);
        } else {
            post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET, User[].class, selectedEvent);
        }
    }

    @Override
    public void onLongClick(Serializable objectAtClicked) {
        final Event event = (Event) objectAtClicked;
        new AlertDialog.Builder(getActivity()).setTitle(R.string.label_delete)
                .setMessage(getText(R.string.label_delete_this_event) + event.getPlace() +
                        "\n" + event.getFullStreetAddress())
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

    private void requestEventList() {
        Event eventWildcard = new Event(Account.getUser().getEmail(), "", "", "", "", "", "",
                "", "", "", "");
        post(Api.EVENTS + Api.GET, Event[].class, eventWildcard);
    }

}
