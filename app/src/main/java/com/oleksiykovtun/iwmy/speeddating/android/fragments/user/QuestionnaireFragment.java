package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.common.UserQuestionnaireFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class QuestionnaireFragment extends UserQuestionnaireFragment {

    private Event event = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_questionnaire, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_send);
        registerClickListener(R.id.button_settings);

        setText(R.id.button_send, "" + getText(R.string.label_waiting_for_organizer));
        setButtonEnabled(R.id.button_send, false);

        event = (Event) getAttachment();

        setText(R.id.label_organizer, event.getPlace());

        // generate ratings for this attendant
        post(Api.RATINGS + Api.GET_FOR_ATTENDANCE_ACTIVE, Rating[].class,
                new Attendance(Account.getUser(), event));

        setupRecyclerView((RecyclerView) view.findViewById(R.id.rating_list_holder));;

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        super.onReceiveWebData(postTag, response);
        switch (postTag) {
            case Api.RATINGS + Api.GET_FOR_ATTENDANCE_ACTIVE:
                startTimer();
                break;
            case Api.EVENTS + Api.GET_FOR_TIME:
                if (response.size() > 0) {
                    event = (Event) response.get(0);
                    if (event.getAllowSendingRatings().equals("true")) {
                        setText(R.id.button_send, "" + getText(R.string.button_send));
                        setButtonEnabled(R.id.button_send, true);
                    }
                }
                ratingRecyclerAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected int getMaxRatingsCount() {
        return CoolFormatter.parseInt(event.getMaxRatingsPerUser());
    }

    @Override
    protected void confirm() {
        CoolFragmentManager.show(new WaitRatingsFragment(), event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                sendRating();
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
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
        if (!ratingList.isEmpty()) {
            startTimer();
        }
    }

}
