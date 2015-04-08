package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
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
public class WaitFragment extends CoolFragment {

    private List<User> userListGuys = new ArrayList<User>();
    private List<User> userListLadies = new ArrayList<User>();

    private Event event = null;
    private static CountDownTimer timer;
    private boolean usersReceived = false;

    private UserRecyclerAdapter userRecyclerAdapterGuys
            = new UserRecyclerAdapter(userListGuys);
    private UserRecyclerAdapter userRecyclerAdapterLadies
            = new UserRecyclerAdapter(userListLadies);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_wait, container, false);
        registerContainerView(view);

        event = (Event) getAttachment();

        RecyclerView userRecyclerViewGuys = (RecyclerView) view
                .findViewById(R.id.user_list_holder_guys);
        userRecyclerViewGuys.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(userRecyclerAdapterGuys);
        userRecyclerViewGuys.setAdapter(userRecyclerAdapterGuys);

        RecyclerView userRecyclerViewLadies = (RecyclerView) view
                .findViewById(R.id.user_list_holder_ladies);
        userRecyclerViewLadies.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(userRecyclerAdapterLadies);
        userRecyclerViewLadies.setAdapter(userRecyclerAdapterLadies);

        ViewPager pager = (ViewPager) view.findViewById(R.id.events_pager);
        pager.setAdapter(new CoolPagerAdapter(this,
                R.id.page_users_guys, R.id.page_users_ladies));

        post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE, User[].class, event);

        return view;
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        User user = (User) objectAtClicked;
        // organizer can vote for a user created by him
        if (user.getPassword().isEmpty()) {
            CoolFragmentManager.showAtTop(new QuestionnaireFragment(), new Attendance(user, event));
        } else {
            showToastLong(R.string.message_you_cannot_add_ratings);
        }
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE:
                if (response.size() > 0) {
                    userListGuys.clear();
                    userListLadies.clear();
                    for (User user : (List<User>) response) {
                        if (user.getGender().equals(User.MALE)) {
                            userListGuys.add(user);
                        } else {
                            userListLadies.add(user);
                        }
                    }
                    userRecyclerAdapterGuys.notifyDataSetChanged();
                    userRecyclerAdapterLadies.notifyDataSetChanged();
                    usersReceived = true;
                    startTimer();
                }
                break;
            case Api.ATTENDANCES + Api.GET_FOR_EVENT_ACTIVE_CHECK_VOTED:
                boolean allUsersVoted = true;
                for (Attendance attendance : (List<Attendance>)response) {
                    if (! attendance.getActive().equals("false")) {
                        highlightUserByAttendance(attendance);
                    } else {
                        allUsersVoted = false;
                    }
                }
                if (allUsersVoted && response.size() > 0) {
                    CoolFragmentManager.show(new CoupleListConfirmFragment(), event);
                }
                break;
        }
    }

    private void highlightUserByAttendance(Attendance attendance) {
        for (int i = 0; i < userListGuys.size(); ++i) {
            User user = userListGuys.get(i);
            if (user.getEmail().equals(attendance.getUserEmail())) {
                getViewInRecyclerView(R.id.user_list_holder_guys, i)
                        .setBackgroundColor(Color.BLUE);
            }
        }
        for (int i = 0; i < userListLadies.size(); ++i) {
            User user = userListLadies.get(i);
            if (user.getEmail().equals(attendance.getUserEmail())) {
                getViewInRecyclerView(R.id.user_list_holder_ladies, i)
                        .setBackgroundColor(Color.BLUE);
            }
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(3600000, 6000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // checking until all active attendants put ratings
                post(Api.ATTENDANCES + Api.GET_FOR_EVENT_ACTIVE_CHECK_VOTED, Attendance[].class,
                        event);
            }

            @Override
            public void onFinish() {
            }

        }.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (usersReceived) {
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

}
