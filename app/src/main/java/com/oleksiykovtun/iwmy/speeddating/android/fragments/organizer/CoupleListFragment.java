package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.CoupleRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class CoupleListFragment extends CoolFragment {

    private List<Couple> coupleList = new ArrayList<Couple>();
    private Event event = null;

    private CoupleRecyclerAdapter coupleRecyclerAdapter = new CoupleRecyclerAdapter(coupleList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_couple_list, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_send_results);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        RecyclerView coupleRecyclerView = (RecyclerView) view.findViewById(R.id.couple_list_holder);
        coupleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(coupleRecyclerAdapter);
        coupleRecyclerView.setAdapter(coupleRecyclerAdapter);

        post(Api.COUPLES + Api.GENERATE_FOR_EVENT, Couple[].class, event);

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.COUPLES + Api.GENERATE_FOR_EVENT:
                coupleList.clear();
                coupleList.addAll(response);
                coupleRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET:
                post(Api.COUPLES + Api.PUT, Couple[].class, coupleList.toArray());
                break;
            case Api.COUPLES + Api.PUT:
                post(Api.EVENTS + Api.SET_UNACTUAL, Event[].class, event);
                break;
            case Api.EVENTS + Api.SET_UNACTUAL:
                showToast(R.string.message_couples_sent);
                CoolFragmentManager.showAtBottom(new MyEventListFragment());
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_results:
                post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET, User[].class, event);
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

}
