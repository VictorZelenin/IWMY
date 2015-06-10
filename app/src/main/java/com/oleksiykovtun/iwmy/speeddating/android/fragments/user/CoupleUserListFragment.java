package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class CoupleUserListFragment extends AppFragment {

    private List<User> coupleUserList = new ArrayList<User>();
    private Event event = null;

    private UserRecyclerAdapter coupleUserRecyclerAdapter
            = new UserRecyclerAdapter(coupleUserList, false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_couple_user_list, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        RecyclerView coupleUserRecyclerView = (RecyclerView) view
                .findViewById(R.id.couple_user_list_holder);
        coupleUserRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(coupleUserRecyclerAdapter);
        coupleUserRecyclerView.setAdapter(coupleUserRecyclerAdapter);

        post(Api.COUPLES + Api.GET_FOR_ATTENDANCE, Couple[].class,
                new Attendance(Account.getUser(), event));

        return view;
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.COUPLES + Api.GET_FOR_ATTENDANCE:
                List<User> coupleUserWildcards = new ArrayList<>();
                for (Couple couple : (List<Couple>) response) {
                    String coupleUserEmail
                            = (Account.getUser().getEmail().equals(couple.getUserEmail2()))
                            ? couple.getUserEmail1() : couple.getUserEmail2();
                    coupleUserWildcards.add(new User(coupleUserEmail, "", "", "", "", "", "", "",
                            "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
                post(Api.USERS + Api.GET, User[].class, coupleUserWildcards.toArray());
                break;
            case Api.USERS + Api.GET:
                coupleUserList.clear();
                for (User user : (List<User>) response) {
                    coupleUserList.add(user);
                }
                coupleUserRecyclerAdapter.notifyDataSetChanged();
                if (coupleUserList.size() == 1) {
                    setText(R.id.label_couple_list, R.string.label_old_couple_list_one);
                } else if (coupleUserList.size() > 1) {
                    setText(R.id.label_couple_list, R.string.label_old_couple_list_many);
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

    @Override
    public void onClick(Serializable objectAtClicked) {
        CoolFragmentManager.showAtTop(new UserProfileCoupleFragment(), objectAtClicked);
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
