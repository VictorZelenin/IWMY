package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.EventRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
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

    private EventRecyclerAdapter actualEventRecyclerAdapter
            = new EventRecyclerAdapter(actualEventList);
    private EventRecyclerAdapter pastEventRecyclerAdapter
            = new EventRecyclerAdapter(pastEventList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_my_event_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_options);
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

        post(Api.EVENTS + Api.GET_FOR_USER, Event[].class, Account.getUser());

        return view;
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.EVENTS + Api.GET_FOR_USER:
                actualEventList.clear();
                pastEventList.clear();
                for (Event event : (List<Event>) response) {
                    if (!event.getActual().equals("false")) {
                        actualEventList.add(event);
                    } else {
                        pastEventList.add(event);
                    }
                }
                actualEventRecyclerAdapter.notifyDataSetChanged();
                pastEventRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.ATTENDANCES + Api.DELETE:
                post(Api.EVENTS + Api.GET_FOR_USER, Event[].class, Account.getUser());
                break;
        }
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

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.showAtTop(new EventAttendFragment(), objectAtClicked);
    }

    @Override
    public void onLongClick(final Serializable objectAtClicked) {
        final Event event = (Event) objectAtClicked;
        new AlertDialog.Builder(getActivity()).setTitle(R.string.label_delete)
                .setMessage(getText(R.string.label_delete_this_event_from_yours)
                        + event.getPlace() + " " + event.getFullStreetAddress())
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        post(Api.ATTENDANCES + Api.DELETE, Attendance[].class,
                                new Attendance(Account.getUser(), (Event)objectAtClicked));
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
