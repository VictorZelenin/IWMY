package com.oleksiykovtun.iwmy.speeddating.android.fragments.common;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.data.User;


/**
 * Created by alx on 2015-02-12.
 */
public abstract class ProfileEditFragment extends EditFragment {

    @Override
    public void onDateSet(String dateString) {
        setText(R.id.label_birth_date, dateString);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
        }
    }


    protected boolean check(User user) {
        return check(user, true);
    }

    protected boolean checkWithoutPassword(User user) {
        return check(user, false);
    }

    protected User makeUser() {
        return makeUser(true);
    }

    protected User makeUserWithoutPassword() {
        return makeUser(false);
    }

    protected void fillForms(User user) {
        setText(R.id.input_email, user.getEmail());
        if (!user.getPassword().isEmpty()) {
            setText(R.id.input_password, user.getPassword());
        }
        setText(R.id.input_username, user.getUsername());
        setText(R.id.input_name_and_surname, user.getNameAndSurname());
        setText(R.id.input_phone, user.getPhone());
        setText(R.id.label_birth_date, user.getBirthDate());
        ((RadioButton) getViewById(R.id.gender_male)).setChecked(
                user.getGender().equals(User.MALE));
        ((RadioButton) getViewById(R.id.gender_female)).setChecked(
                user.getGender().equals(User.FEMALE));

        if (! user.getPhoto().isEmpty()) {
            ImageManager.setUserPhoto(getImageView(R.id.photo), user.getGender(),
                    user.getPhoto());
            ((Button) getViewById(R.id.button_photo)).setText(R.string.button_remove_photo);
            defaultPhotoLink = user.getPhoto();
            defaultThumbnailLink = user.getThumbnail();
        }
    }

    private boolean check(User user, boolean includingPassword) {
        return user.getEmail().contains("@") && !user.getEmail().contains(":")
                && !user.getNameAndSurname().isEmpty()
                && (!includingPassword || !user.getPassword().isEmpty())
                && !user.getUsername().isEmpty()
                && CoolFormatter.isDateValid(user.getBirthDate());
    }

    private User makeUser(boolean includingPassword) {
        User user = new User();
        user.setEmail(getEditText(R.id.input_email));
        user.setPassword(includingPassword ? getEditText(R.id.input_password) : "");
        user.setUsername(getEditText(R.id.input_username));
        user.setGroup(User.USER);
        user.setNameAndSurname(getEditText(R.id.input_name_and_surname));
        user.setPhoto(photoChanged
                ? ImageManager.getBase64StringFromBitmap(photoBitmap, 90) : defaultPhotoLink);
        user.setThumbnail(photoChanged
                ? ImageManager.getBase64StringFromBitmap(thumbnailBitmap, 85) : defaultThumbnailLink);
        user.setPhone(getEditText(R.id.input_phone));
        user.setBirthDate(getLabelText(R.id.label_birth_date));
        user.setGender(isRadioButtonChecked(R.id.gender_male) ? User.MALE : User.FEMALE);
        user.setOrientation("");
        user.setGoal("");
        user.setAffair("");
        user.setHeight("");
        user.setWeight("");
        user.setAttitudeToSmoking("");
        user.setAttitudeToAlcohol("");
        user.setLocation("");
        user.setOrganization("");
        user.setWebsite("");
        user.setIsChecked("false");
        user.setReferralEmail(includingPassword ? "" : Account.getUser().getEmail());
        return user;
    }

}
