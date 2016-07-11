package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.BuildConfig;
import com.jacksonsmolenko.iwmy.R;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

public class RegistrationFragment extends com.jacksonsmolenko.iwmy.fragments.common.ProfileEditFragment {

    private String password1;
    private String password2;
    private User impliedUser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_select_date);
        registerClickListener(R.id.button_register);
        password1 = getEditText(R.id.input_password);
        password2 = getEditText(R.id.input_password);
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_register:
                String email = getEditText(R.id.input_email);
                String password = getEditText(R.id.input_password);
                String username = getEditText(R.id.input_username);
                String group = User.PENDING_ORGANIZER;
                String nameAndSurname = getEditText(R.id.input_name);
                String photo = "";
                String thumbnail = "";
                String phone = getEditText(R.id.input_phone);
                String birthDate = "";
                String city = getEditText(R.id.input_city);
                String country = getEditText(R.id.input_country);
                String gender = isRadioButtonChecked(R.id.gender_male) ? User.MALE : User.FEMALE;
                String orientation = "";
                String goal = "";
                String affair = "";
                String height = "";
                String weight = "";
                String attitudeToSmoking = "";
                String attitudeToAlcohol = "";
                String location = "";
                String organization = "";
                String website = "";
                String referralEmail = "";

                impliedUser = new User(email, password, username, group, nameAndSurname,
                        photo, thumbnail, phone, birthDate, city, country, gender, orientation,
                        goal, affair, height, weight, attitudeToSmoking, attitudeToAlcohol,
                        location, organization, website, referralEmail);
                if (checkUser(impliedUser)) {
                    post(Api.USERS + Api.ADD_PENDING_ORGANIZER, User[].class, impliedUser);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.USERS + Api.ADD_PENDING_ORGANIZER:
                if (!response.isEmpty()) {
                    post(Api.MAIL + Api.REQUEST_ORGANIZER, Email[].class,
                            getOrganizerRequestEmail(impliedUser),
                            getOrganizerConfirmationEmail(impliedUser));
                } else {
                    showToastLong(R.string.message_user_exists);
                }
                break;
            case Api.MAIL + Api.REQUEST_ORGANIZER:
                new AppliedDialogFragment();
                break;
        }
    }

    private boolean checkUser(User user) {
        return user.getEmail().contains("@") && !user.getEmail().contains(":")
                && !user.getName().isEmpty()
                && !user.getPassword().isEmpty()
                && !user.getUsername().isEmpty()
                && !user.getPhone().isEmpty();
    }

    private Email getOrganizerConfirmationEmail(User organizerUser) {
        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name), organizerUser.getEmail(),
                organizerUser.getNameAndSurname(),
                "" + getText(R.string.mail_subject_organizer_confirmation),
                ("" + getText(R.string.mail_text_organizer_confirmation))
                        .replace("CONTACTS_SPEED_DATING", Api.APP_SUPPORT_EMAIL));
    }

    private Email getOrganizerRequestEmail(User organizerUser) {
        String message = "A new organizer wants to be registered:\n";
        message += "\nEmail: " + organizerUser.getEmail();
        message += "\nUsername: " + organizerUser.getUsername();
        message += "\nNameAndSurname: " + organizerUser.getNameAndSurname();
        message += "\nPhone: " + organizerUser.getPhone();
        message += "\nOrganization: " + organizerUser.getOrganization();
        message += "\nWebsite: " + organizerUser.getWebsite() + "\n\n";

        message += "Follow the link to activate this organizer:\n";
        message += BuildConfig.BACKEND_URL + Api.USERS + Api.ACTIVATE_PENDING_ORGANIZER;
        message += "/:" + organizerUser.getEmail();
        message += "\n\nThe organizer will be informed about activation by email automatically.\n";

        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name), Api.APP_SUPPORT_EMAIL,
                "" + getText(R.string.app_name), "" + getText(R.string.mail_subject_organizer_registering), message);
    }
}
