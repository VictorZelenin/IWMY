package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.TimeConverter;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.data.User;

/**
 * Created by alx on 2015-02-12.
 */
public class UserProfileFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_user_profile, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        User user = (User) getAttachment();

        ImageManager.setUserPic(getImageView(R.id.image_user_pic), user);
            setText(R.id.label_name_and_age, user.getNameAndSurname() + ", "
                    + TimeConverter.getYearsFromDate(user.getBirthDate()));
        setText(R.id.label_name_and_surname,
                R.string.label_name_and_surname, user.getNameAndSurname());
        setText(R.id.label_birth_date, R.string.label_birth_date, user.getBirthDate());
        setText(R.id.label_gender, R.string.label_gender, user.getGender());
        setText(R.id.label_email, R.string.label_email, user.getEmail());
        setText(R.id.label_phone, R.string.label_phone, user.getPhone());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

}
