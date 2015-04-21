package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.TimeConverter;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer.SettingsFragment;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.user.*;
import com.oleksiykovtun.iwmy.speeddating.data.User;

/**
 * Created by alx on 2015-02-12.
 */
public class UserProfileFragment extends CoolFragment {

    User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_user_profile, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_settings);

        user = (User) getAttachment();

        // only users added by organizer are editable
        if (user.getPassword().isEmpty()) {
            registerClickListener(R.id.button_options);
        } else {
            ((ViewManager)getViewById(R.id.button_options).getParent())
                    .removeView(getViewById(R.id.button_options));
        }

        ImageManager.setUserPic(getImageView(R.id.image_user_pic), user);
        setText(R.id.label_user, R.string.toolbar_user, "- " + user.getUsername());
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
            case R.id.button_options:
                openMenu(view);
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CoolFragmentManager.show(new UserProfileEditFragment(), user);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.user_profile, menu);
    }

}
