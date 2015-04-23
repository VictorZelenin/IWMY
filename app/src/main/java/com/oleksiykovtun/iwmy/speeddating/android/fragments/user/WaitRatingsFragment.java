package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * The 1st stage of user waiting: waiting for all users to put ratings
 */
public class WaitRatingsFragment extends CoolFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_wait_ratings, container, false);
        registerContainerView(view);

        event = (Event) getAttachment();

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        List<Attendance> attendances = (List<Attendance>) response;
        int votedCount = 0;
        for (Attendance attendance : attendances) {
            if (attendance.getActive().equals("true")) {
                ++votedCount;
            }
        }
        setText(R.id.label_progress, votedCount + "/" + attendances.size());
        if (attendances.size() > 0 && votedCount == attendances.size()) {
            CoolFragmentManager.show(new WaitCouplesFragment(), event);
        }
    }

    @Override
    protected void onTimerTick() {
        // checking until for this attendant couples are put
        post(Api.ATTENDANCES + Api.GET_FOR_EVENT_ACTIVE_CHECK_VOTED, Attendance[].class, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

}
