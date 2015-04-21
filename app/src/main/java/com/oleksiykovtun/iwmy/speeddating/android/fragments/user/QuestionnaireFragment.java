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

        event = (Event) getAttachment();

        setText(R.id.label_organizer, event.getPlace());

        // generate ratings for this attendant
        post(Api.RATINGS + Api.GET_FOR_ATTENDANCE_ACTIVE, Rating[].class,
                new Attendance(Account.getUser(this), event));

        setupRecyclerView((RecyclerView) view.findViewById(R.id.rating_list_holder));;

        return view;
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

}
