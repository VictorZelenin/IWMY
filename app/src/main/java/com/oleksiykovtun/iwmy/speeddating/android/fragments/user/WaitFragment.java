package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
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
        View view = inflater.inflate(R.layout.fragment_user_wait, container, false);
        registerContainerView(view);

        event = (Event)getAttachment();

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        if (response.size() > 0) {
            CoolFragmentManager.switchToRootFragment(new UserProfileCoupleFragment(),
                    (Couple) response.get(0));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        timer = new CountDownTimer(3600000, 6000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // checking until for this attendant couples are put
                post("http://iwmy-speed-dating.appspot.com/couples/get/for/attendance",
                        Couple[].class,
                        new Attendance(Account.getUser(WaitFragment.this), event));
            }

            @Override
            public void onFinish() { }

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
