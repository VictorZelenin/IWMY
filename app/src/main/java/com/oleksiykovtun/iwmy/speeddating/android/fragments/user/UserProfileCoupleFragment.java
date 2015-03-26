package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class UserProfileCoupleFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_user_profile_couple, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        Couple couple = (Couple) getAttachment();
        String coupleUserEmail
                = (Account.getUser(this).getEmail().equals(couple.getUserEmail2()))
                ? couple.getUserEmail1() : couple.getUserEmail2();
        User coupleUser = new User(coupleUserEmail,
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        post(Api.USERS + Api.GET, User[].class, coupleUser);

        return view;
    }

    @Override
    public void onReceiveWebData(List response) {
        User coupleUser;
        if (!response.isEmpty() && response.size() > 0) {
            coupleUser = (User) response.get(0);
            setImageFromBase64String(R.id.image_user_pic, coupleUser.getPhotoBase64());
            setText(R.id.label_couple, coupleUser.getNameAndSurname());
            setText(R.id.label_name_and_surname, R.string.label_name_and_surname,
                    coupleUser.getNameAndSurname());
            setText(R.id.label_email, R.string.label_email,
                    coupleUser.getEmail());
            setText(R.id.label_phone, R.string.label_phone,
                    coupleUser.getPhone());
            setText(R.id.label_birth_date, R.string.label_birth_date,
                    coupleUser.getBirthDate());
            setText(R.id.label_gender, R.string.label_gender,
                    coupleUser.getGender());
            setText(R.id.label_orientation, R.string.label_orientation,
                    coupleUser.getOrientation());
            // todo add missing field to User
            setText(R.id.label_appearance, R.string.label_appearance,
                    "");
            setText(R.id.label_goal, R.string.label_goal,
                    "");
            setText(R.id.label_affairs, R.string.label_affairs,
                    "");
            // todo add missing field to User
            setText(R.id.label_children, R.string.label_children,
                    "");
            setText(R.id.label_height, R.string.label_height,
                    coupleUser.getHeight());
            setText(R.id.label_weight, R.string.label_weight,
                    coupleUser.getWeight());
            setText(R.id.label_attitude_to_smoking, R.string.label_attitude_to_smoking,
                    coupleUser.getAttitudeToSmoking());
            setText(R.id.label_attitude_to_alcohol, R.string.label_attitude_to_alcohol,
                    coupleUser.getAttitudeToAlcohol());
        } else {
            // todo xml
            Log.e("IWMY", "No couple found.");
            showToastLong("No couple found.");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_settings:
                CoolFragmentManager.switchToFragment(new SettingsFragment());
                break;
        }
    }

}
