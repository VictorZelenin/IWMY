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

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolPagerAdapter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.UserRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class ParticipantListFragment extends CoolFragment {

    private List<User> userListGuys = new ArrayList<User>();
    private List<User> userListLadies = new ArrayList<User>();
    private Event event = null;

    private UserRecyclerAdapter userRecyclerAdapterGuys
            = new UserRecyclerAdapter(userListGuys);
    private UserRecyclerAdapter userRecyclerAdapterLadies
            = new UserRecyclerAdapter(userListLadies);

    @Override
    public void onStart() {
        super.onStart();
        post(Api.USERS + Api.GET_FOR_EVENT, User[].class, event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_participant_list, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_add_participant);
        registerClickListener(R.id.button_settings);

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

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_participant:
                CoolFragmentManager.switchToFragment(new NewParticipantFragment(), event);
                break;
            case R.id.button_settings:
                CoolFragmentManager.switchToFragment(new SettingsFragment());
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.switchToFragment(new UserProfileFragment(), objectAtClicked);
    }

    @Override
    public void onLongClick(Serializable objectAtClicked) {
        final User user = (User) objectAtClicked;
        new AlertDialog.Builder(getActivity()).setTitle(R.string.label_delete)
                .setMessage(getText(R.string.label_remove_this_user_from_event)
                        + user.getNameAndSurname() + " " + user.getEmail())
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        post(Api.USERS + Api.REMOVE_ATTENDANCE, User[].class,
                                new Attendance(user, event));
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
