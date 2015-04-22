package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.graphics.Color;
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
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.UserRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class SelectParticipantFragment extends CoolFragment {

    private List<User> userList = new ArrayList<User>();

    private Event event = null;

    private UserRecyclerAdapter userRecyclerAdapter = new UserRecyclerAdapter(userList, Color.GREEN);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_select_participant, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        RecyclerView userRecyclerViewGuys = (RecyclerView) view
                .findViewById(R.id.user_list_holder);
        userRecyclerViewGuys.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(userRecyclerAdapter);
        userRecyclerViewGuys.setAdapter(userRecyclerAdapter);
        registerItemClickListener(userRecyclerAdapter);

        post(Api.USERS + Api.GET_OTHER_FOR_EVENT, User[].class, event);

        return view;
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        post(Api.ATTENDANCES + Api.ADD, Attendance[].class,
                new Attendance((User) objectAtClicked, event));
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.USERS + Api.GET_OTHER_FOR_EVENT:
                userList.clear();
                userList.addAll(response);
                userRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.ATTENDANCES + Api.ADD:
                if (!response.isEmpty()) {
                    showToast(R.string.message_participant_added);
                    CoolFragmentManager.showPrevious();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

}
