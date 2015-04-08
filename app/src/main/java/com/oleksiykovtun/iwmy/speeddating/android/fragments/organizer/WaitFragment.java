package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.os.CountDownTimer;
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
 * Created by alx on 2015-02-12.
 */
public class WaitFragment extends CoolFragment {

    private Event event = null;
    private static CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_wait, container, false);
        registerContainerView(view);

        event = (Event) getAttachment();

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        if (response.size() > 0) {
            CoolFragmentManager.show(new CoupleListConfirmFragment(), event);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        timer = new CountDownTimer(3600000, 6000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // checking until all active attendants put ratings
                post(Api.ATTENDANCES + Api.CHECK_FOR_EVENT_ACTIVE_ALL, Attendance[].class, event);
            }

            @Override
            public void onFinish() {
            }

        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

}
