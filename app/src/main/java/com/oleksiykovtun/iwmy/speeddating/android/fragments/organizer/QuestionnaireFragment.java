package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.common.UserQuestionnaireFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

/**
 * Created by alx on 2015-02-12.
 */
public class QuestionnaireFragment extends UserQuestionnaireFragment {

    private Attendance attendance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_questionnaire, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_send);

        attendance = (Attendance) getAttachment();

        // generate ratings for all active attendants of this event
        post(Api.RATINGS + Api.GET_FOR_ATTENDANCE_ACTIVE, Rating[].class, attendance);

        setupRecyclerView((RecyclerView) view.findViewById(R.id.rating_list_holder));

        return view;
    }

    @Override
    protected void confirm() {
        CoolFragmentManager.showPrevious();
    }

    @Override
    protected int getMaxRatingsCount() {
        return CoolApplication.readPreferences(SettingsFragment.MAX_RATINGS, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                sendRating();
                break;
        }
    }

}
