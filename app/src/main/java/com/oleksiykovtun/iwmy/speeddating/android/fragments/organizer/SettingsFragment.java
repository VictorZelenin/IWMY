package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.StartFragment;


/**
 * Created by alx on 2015-02-04.
 */
public class SettingsFragment extends AppFragment {

    public static final String MAX_RATINGS = "maxRatings";
    public static final String SEND_COUPLE_EMAILS = "sendCoupleEmails";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_settings, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_logout);

        setMaxRatingsPerUser();
        setSendCoupleEmails();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                Account.removeUser();
                CoolFragmentManager.showAtBottom(new StartFragment());
        }
    }

    private void setMaxRatingsPerUser() {
        int maxRatingsPerUser = CoolApplication.readPreferences(MAX_RATINGS, 1);

        setText(R.id.label_max_ratings, R.string.label_max_ratings, " " + maxRatingsPerUser);

        SeekBar maxRatingsSeekBar = (SeekBar) getViewById(R.id.seekbar_max_ratings);
        maxRatingsSeekBar.setProgress(maxRatingsPerUser - 1);

        maxRatingsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int maxRatingsPerUser = progress + 1;
                CoolApplication.writePreferences(MAX_RATINGS, maxRatingsPerUser);
                setText(R.id.label_max_ratings, R.string.label_max_ratings, "" + maxRatingsPerUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setSendCoupleEmails() {
        boolean sendCoupleEmails = CoolApplication.readPreferences(SEND_COUPLE_EMAILS, true);

        CheckBox sendCoupleEmailsCheckBox = (CheckBox) getViewById(R.id.checkbox_send_couple_emails);
        sendCoupleEmailsCheckBox.setChecked(sendCoupleEmails);

        sendCoupleEmailsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CoolApplication.writePreferences(SEND_COUPLE_EMAILS, isChecked);
            }
        });
    }
}
