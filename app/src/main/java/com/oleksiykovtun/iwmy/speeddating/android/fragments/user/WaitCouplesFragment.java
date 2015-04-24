package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * The 2nd stage of user waiting: waiting for organizer to confirm couples
 */
public class WaitCouplesFragment extends AppFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_wait_couples, container, false);
        registerContainerView(view);

        event = (Event) getAttachment();

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        if (response.size() > 0) {
            event = (Event) response.get(0);
            if (event.getActual().equals("false")) {
                CoolFragmentManager.showAtBottom(new CoupleUserListFragment(), event);
            }
        }
    }

    @Override
    protected void onTimerTick() {
        // checking until the event is already not actual (i.e. couples are obtained)
        post(Api.EVENTS + Api.GET_FOR_TIME, Event[].class, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

}
