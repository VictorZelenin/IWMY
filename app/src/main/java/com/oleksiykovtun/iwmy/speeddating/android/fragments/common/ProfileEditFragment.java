package com.oleksiykovtun.iwmy.speeddating.android.fragments.common;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.User;

/**
 * Created by alx on 2015-02-12.
 */
public abstract class ProfileEditFragment extends CoolFragment {

    @Override
    public void onDateSet(String dateString) {
        setText(R.id.label_birth_date, dateString);
    }

    protected boolean check(User user) {
        return check(user, true);
    }

    protected boolean checkWithoutPassword(User user) {
        return check(user, false);
    }

    protected User fill(User user) {
        return fill(user, true);
    }

    protected User fillWithoutPassword(User user) {
        return fill(user, false);
    }

    private boolean check(User user, boolean includingPassword) {
        return user.getEmail().contains("@")
                && !user.getNameAndSurname().isEmpty()
                && (!includingPassword || !user.getPassword().isEmpty())
                && !user.getUsername().isEmpty()
                && CoolFormatter.isDateValid(user.getBirthDate());
    }

    private User fill(User user, boolean includingPassword) {
        user.setEmail(getEditText(R.id.input_email));
        user.setPassword(includingPassword ? getEditText(R.id.input_password) : "");
        user.setUsername(getEditText(R.id.input_username));
        user.setGroup(User.USER);
        user.setNameAndSurname(getEditText(R.id.input_name_and_surname));
        user.setPhotoBase64("");
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
        return user;
    }

}
