package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.UserRecyclerAdapter;
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
public class WaitFragment extends AppFragment {

    private List<User> userListGuys = new ArrayList<User>();
    private List<User> userListLadies = new ArrayList<User>();

    private Event event = null;
    private boolean usersReceived = false;

    private UserRecyclerAdapter userRecyclerAdapterGuys
            = new UserRecyclerAdapter(userListGuys, Color.GREEN);
    private UserRecyclerAdapter userRecyclerAdapterLadies
            = new UserRecyclerAdapter(userListLadies, Color.GREEN);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_wait, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_allow_users_send_ratings);

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

        ViewPager pager = (ViewPager) view.findViewById(R.id.users_pager);
        pager.setAdapter(new CoolPagerAdapter(this,
                R.id.page_users_guys, R.id.page_users_ladies));

        post(Api.EVENTS + Api.GET_FOR_TIME, Event[].class, event);

        return view;
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        User user = (User) objectAtClicked;
        // organizer can vote for a user created by him
        if (user.getPassword().isEmpty()) {
            CoolFragmentManager.showAtTop(new QuestionnaireFragment(), new Attendance(user, event));
        } else {
            showToastLong(R.string.message_you_cannot_add_ratings_user);
        }
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.EVENTS + Api.GET_FOR_TIME:
                if (response.size() > 0) {
                    event = (Event) response.get(0);
                    updateUserRatingsAllowButton();
                    post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE, User[].class, event);
                }
                break;
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
                    updateLists();
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
            case Api.EVENTS + Api.SET_USER_RATINGS_ALLOW:
                if (response.size() == 1) {
                    event = (Event) response.get(0); // already allows user ratings
                    updateUserRatingsAllowButton();
                }
                break;
        }
    }

    private void highlightUserByAttendance(Attendance attendance) {
        for (int i = 0; i < userListGuys.size(); ++i) {
            User user = userListGuys.get(i);
            if (user.getEmail().equals(attendance.getUserEmail())) {
                user.setIsChecked("true");
            }
        }
        for (int i = 0; i < userListLadies.size(); ++i) {
            User user = userListLadies.get(i);
            if (user.getEmail().equals(attendance.getUserEmail())) {
                user.setIsChecked("true");
            }
        }
        updateLists();
    }

    private void updateLists() {
        userRecyclerAdapterGuys.notifyDataSetChanged();
        int highlightedGuysCount = countHighlightedUsers(userListGuys);
        getViewById(R.id.page_users_guys).setTag(getText(R.string.tab_guys)
                + ((highlightedGuysCount > 0) ? (" (" + highlightedGuysCount + ")") : ""));

        userRecyclerAdapterLadies.notifyDataSetChanged();
        int highlightedLadiesCount = countHighlightedUsers(userListLadies);
        getViewById(R.id.page_users_ladies).setTag(getText(R.string.tab_ladies)
                + ((highlightedLadiesCount > 0) ? (" (" + highlightedLadiesCount + ")") : ""));

        ((ViewPager) getViewById(R.id.users_pager)).getAdapter().notifyDataSetChanged();
    }

    private int countHighlightedUsers(List<User> users) {
        int highlightedUserCount = 0;
        for (User user : users) {
            if (user.getIsChecked().equals("true")) {
                highlightedUserCount++;
            }
        }
        return highlightedUserCount;
    }

    @Override
    protected void onTimerTick() {
        // checking until all active attendants put ratings
        if (!isPostRequestRunningNow()) {
            post(Api.ATTENDANCES + Api.GET_FOR_EVENT_ACTIVE_CHECK_VOTED, Attendance[].class,
                    event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_allow_users_send_ratings:
                post(Api.EVENTS + Api.SET_USER_RATINGS_ALLOW, Event[].class, event);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usersReceived) {
            startTimer();
        }
    }

    private void updateUserRatingsAllowButton() {
        getViewById(R.id.button_allow_users_send_ratings).setVisibility(
                event.getAllowSendingRatings().equals("true") ? View.GONE : View.VISIBLE);
    }

}
