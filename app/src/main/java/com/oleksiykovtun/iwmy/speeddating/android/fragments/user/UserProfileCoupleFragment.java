package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;


/**
 * Created by alx on 2015-02-12.
 */
public class UserProfileCoupleFragment extends AppFragment {

    private User coupleUser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_user_profile_couple, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        coupleUser = (User) getAttachment();

        ImageManager.setUserPhoto(getImageView(R.id.photo), coupleUser.getGender(),
                coupleUser.getPhoto());
        setText(R.id.label_couple, coupleUser.getNameAndSurname());
        setText(R.id.label_name_and_surname, R.string.label_name_and_surname,
                coupleUser.getNameAndSurname());
        setText(R.id.label_email, R.string.label_email,
                coupleUser.getEmail());
        setText(R.id.label_phone, R.string.label_phone,
                coupleUser.getPhone());
        setText(R.id.label_gender, R.string.label_gender,
                coupleUser.getGender());
        return view;
    }

}
