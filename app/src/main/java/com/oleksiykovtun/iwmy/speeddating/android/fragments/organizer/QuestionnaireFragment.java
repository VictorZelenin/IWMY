package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.RatingRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class QuestionnaireFragment extends CoolFragment {

    private List<Rating> ratingList = new ArrayList<Rating>();
    private Attendance attendance = null;

    private RatingRecyclerAdapter ratingRecyclerAdapter = new RatingRecyclerAdapter(ratingList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_questionnaire, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_send);

        attendance = (Attendance) getAttachment();

        // generate ratings for all active attendants of this event
        post(Api.RATINGS + Api.GENERATE_FOR_ATTENDANCE_ACTIVE, Rating[].class, attendance);

        RecyclerView ratingRecyclerView = (RecyclerView) view.findViewById(R.id.rating_list_holder);
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(ratingRecyclerAdapter);
        ratingRecyclerView.setAdapter(ratingRecyclerAdapter);

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.RATINGS + Api.GENERATE_FOR_ATTENDANCE_ACTIVE:
                ratingList.clear();
                ratingList.addAll(response);
                ratingRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.RATINGS + Api.PUT:
                CoolFragmentManager.showPrevious();
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
        int selectedRatingsCount = 0;
        for (Rating rating : ratingList) {
            if (! rating.getSelection().isEmpty()) {
                ++selectedRatingsCount;
            }
        }
        if (selectedRatingsCount > CoolApplication.readPreferences(SettingsFragment.MAX_RATINGS, 1)) {
            ((Rating)objectAtClicked).setSelection("");
            ratingRecyclerAdapter.notifyDataSetChanged();
            showToast(R.string.message_you_cannot_add_more_ratings);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                if (!ratingList.isEmpty()) {
                    post(Api.RATINGS + Api.PUT, Rating[].class, ratingList.toArray());
                }
                break;
        }
    }

}
