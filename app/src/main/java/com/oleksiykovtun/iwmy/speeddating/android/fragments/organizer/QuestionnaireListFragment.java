package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.UserRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
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
    private int selectedCount = 0;

    private UserRecyclerAdapter userRecyclerAdapterGuys
            = new UserRecyclerAdapter(userListGuys);
    private UserRecyclerAdapter userRecyclerAdapterLadies
            = new UserRecyclerAdapter(userListLadies);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_questionnaire_list, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_add_questionnaire);
        registerClickListener(R.id.button_send_questionnaires);

        event = (Event)getAttachment();

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

        selectedCount = 0;
        post("http://iwmy-speed-dating.appspot.com/users/get/for/event/active/reset",
                User[].class, event);

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        if (response.size() > 0) {
            userListGuys.clear();
            userListLadies.clear();
            for (User user : (List<User>) response) {
                if (user.getGender().equals("male")) {
                    userListGuys.add(user);
                } else {
                    userListLadies.add(user);
                }
            }
            userRecyclerAdapterGuys.notifyDataSetChanged();
            userRecyclerAdapterLadies.notifyDataSetChanged();
            setText(R.id.toolbar_questionnaires, R.string.toolbar_questionnaires_empty, ""
                    + selectedCount + getText(R.string.label_out_of)
                    + (userListGuys.size() + userListLadies.size()));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_questionnaire:
                CoolFragmentManager.switchToFragment(new QuestionnaireOfflineFragment(), event);
                break;
            case R.id.button_send_questionnaires:
                showToast(R.string.message_questionnaires_sent);
                CoolFragmentManager.switchToFragment(new WaitFragment(), event);
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        toggleSelectionBackgroundColor(view);
        // todo post selected after Send button pressed
        post("http://iwmy-speed-dating.appspot.com/attendances/toggle", Attendance[].class,
                new Attendance((User)objectAtClicked, event));
    }

    private void toggleSelectionBackgroundColor(View view) {
        if (isItemSelected(view)) {
            view.setBackgroundColor(Color.TRANSPARENT);
            selectedCount--;
        } else {
            view.setBackgroundColor(Color.GREEN);
            selectedCount++;
        }
        setText(R.id.toolbar_questionnaires, R.string.toolbar_questionnaires_empty, ""
                + selectedCount + getText(R.string.label_out_of)
                + (userListGuys.size() + userListLadies.size()));
    }

    private boolean isItemSelected(View view) {
        return ((ColorDrawable) view.getBackground()).getColor() == Color.GREEN;
    }

}
