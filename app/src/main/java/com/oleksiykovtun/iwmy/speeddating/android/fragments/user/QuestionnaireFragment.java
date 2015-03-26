package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

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
import com.oleksiykovtun.iwmy.speeddating.android.adapters.RatingRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class QuestionnaireFragment extends CoolFragment {

    private List<Rating> ratingList = new ArrayList<>();
    private Event event = null;

    private RatingRecyclerAdapter ratingRecyclerAdapter = new RatingRecyclerAdapter(ratingList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_questionnaire, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_send);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        // generate ratings for this attendant
        post(Api.RATINGS + Api.GENERATE_FOR_ATTENDANCE_ACTIVE, Rating[].class,
                new Attendance(Account.getUser(this), event));

        RecyclerView ratingRecyclerView = (RecyclerView) view.findViewById(R.id.rating_list_holder);
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(ratingRecyclerAdapter);
        ratingRecyclerView.setAdapter(ratingRecyclerAdapter);

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        ratingList.clear();
        ratingList.addAll(response);
        ratingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                if (!ratingList.isEmpty()) {
                    postForNoResult(Api.RATINGS + Api.PUT, ratingList.toArray());
                }
                CoolFragmentManager.switchToFragment(new WaitFragment(), event);
                break;
            case R.id.button_settings:
                CoolFragmentManager.switchToFragment(new SettingsFragment());
                break;
        }
    }

}
