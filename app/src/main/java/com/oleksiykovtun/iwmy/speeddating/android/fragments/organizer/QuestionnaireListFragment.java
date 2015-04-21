package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolApplication;
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
public class QuestionnaireListFragment extends CoolFragment {

    private List<User> userListGuys = new ArrayList<User>();
    private List<User> userListLadies = new ArrayList<User>();
    private Event event = null;
    private int selectedCountMale = 0;
    private int selectedCountFemale = 0;

    private UserRecyclerAdapter userRecyclerAdapterGuys
            = new UserRecyclerAdapter(userListGuys, Color.GREEN);
    private UserRecyclerAdapter userRecyclerAdapterLadies
            = new UserRecyclerAdapter(userListLadies, Color.GREEN);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_questionnaire_list, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_send_questionnaires);

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

        selectedCountMale = 0;
        selectedCountFemale = 0;
        post(Api.USERS + Api.GET_FOR_EVENT, User[].class, event);

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.USERS + Api.GET_FOR_EVENT:
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
                post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE, User[].class, event);
                break;
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE:
                for (User activeUser : (List<User>)response) {
                    highlightActiveUser(activeUser.getEmail());
                }
                updateToolbarTitle();
                break;
            case Api.EVENTS + Api.PUT:
                showToast(R.string.message_questionnaires_sent);
                CoolFragmentManager.showAtTop(new WaitFragment(), event);
                break;
            case Api.ATTENDANCES + Api.TOGGLE:
                for (Attendance attendance : (List<Attendance>)response) {
                    if (attendance.getActive().equals("true")) {
                        highlightActiveUser(attendance.getUserEmail());
                    } else {
                        highlightInactiveUser(attendance.getUserEmail());
                    }
                }
                updateToolbarTitle();
                break;
        }
    }

    private void highlightActiveUser(String activeUserEmail) {
        for (int i = 0; i < userListGuys.size(); ++i) {
            User user = userListGuys.get(i);
            if (user.getEmail().equals(activeUserEmail)) {
                user.setIsChecked("true");
                selectedCountMale++;
            }
        }
        for (int i = 0; i < userListLadies.size(); ++i) {
            User user = userListLadies.get(i);
            if (user.getEmail().equals(activeUserEmail)) {
                user.setIsChecked("true");
                selectedCountFemale++;
            }
        }
        userRecyclerAdapterGuys.notifyDataSetChanged();
        userRecyclerAdapterLadies.notifyDataSetChanged();
    }

    private void highlightInactiveUser(String inactiveUserEmail) {
        for (int i = 0; i < userListGuys.size(); ++i) {
            User user = userListGuys.get(i);
            if (user.getEmail().equals(inactiveUserEmail)) {
                user.setIsChecked("false");
                selectedCountMale--;
            }
        }
        for (int i = 0; i < userListLadies.size(); ++i) {
            User user = userListLadies.get(i);
            if (user.getEmail().equals(inactiveUserEmail)) {
                user.setIsChecked("false");
                selectedCountFemale--;
            }
        }
        userRecyclerAdapterGuys.notifyDataSetChanged();
        userRecyclerAdapterLadies.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_questionnaires:
                if (! isPostRequestRunningNow()) {
                    if (selectedCountMale > 0 && selectedCountMale == selectedCountFemale) {
                        event.setMaxRatingsPerUser(""
                                + CoolApplication.readPreferences(SettingsFragment.MAX_RATINGS, 1));
                        post(Api.EVENTS + Api.PUT, Event[].class, event); // "unlocking" the event
                    } else {
                        showToastLong(R.string.message_select_equal);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        if (! isPostRequestRunningNow()) {
            view.setBackgroundColor(getResources().getColor(R.color.gray));
            post(Api.ATTENDANCES + Api.TOGGLE, Attendance[].class,
                    new Attendance((User) objectAtClicked, event));
        }
    }

    private void updateToolbarTitle() {
        setText(R.id.toolbar_questionnaires, R.string.toolbar_questionnaires_empty, ""
                + (selectedCountMale + selectedCountFemale) + getText(R.string.label_out_of)
                + (userListGuys.size() + userListLadies.size()));
    }

}
