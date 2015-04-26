package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class RegisteringFragment extends AppFragment {

    private User impliedUser = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_registering, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_apply);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_apply:
                String email = getEditText(R.id.input_email);
                String password = getEditText(R.id.input_password);
                String username = getEditText(R.id.input_username);
                String group = User.PENDING_ORGANIZER;
                String nameAndSurname = getEditText(R.id.input_name_and_surname);
                String photoBase64 = "";
                String phone = getEditText(R.id.input_phone);
                String birthDate = "";
                String gender = "";
                String orientation = "";
                String goal = "";
                String affair = "";
                String height = "";
                String weight = "";
                String attitudeToSmoking = "";
                String attitudeToAlcohol = "";
                String location = "";
                String organization = getEditText(R.id.input_organization);
                String website = getEditText(R.id.input_website);
                String referralEmail = "";

                impliedUser = new User(email, password, username, group, nameAndSurname,
                        photoBase64, phone, birthDate, gender, orientation, goal, affair, height,
                        weight, attitudeToSmoking, attitudeToAlcohol, location, organization,
                        website, referralEmail);
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
                            getOrganizerRequestEmail(impliedUser));
                } else {
                    showToastLong(R.string.message_user_exists);
                }
                break;
            case Api.MAIL + Api.REQUEST_ORGANIZER:
                CoolFragmentManager.showAtBottom(new AppliedFragment());
                break;
        }
    }

    private boolean checkUser(User user) {
        return user.getEmail().contains("@") && !user.getEmail().contains(":")
                && !user.getNameAndSurname().isEmpty()
                && !user.getPassword().isEmpty()
                && !user.getUsername().isEmpty()
                && !user.getPhone().isEmpty()
                && !user.getOrganization().isEmpty()
                && !user.getWebsite().isEmpty();
    }

    private Email getOrganizerRequestEmail(User organizerUser) {
        String message = "A new organizer wants to be registered:\n";
        message += "\nEmail: " + organizerUser.getEmail();
        message += "\nUsername: " + organizerUser.getUsername();
        message += "\nNameAndSurname: " + organizerUser.getNameAndSurname();
        message += "\nPhone: " + organizerUser.getPhone();
        message += "\nOrganization: " + organizerUser.getOrganization();
        message += "\nWebsite: " + organizerUser.getWebsite() + "\n\n";

        message += "Follow the link to add the organizer:\n";
        message += BuildConfig.BACKEND_URL + Api.USERS + Api.ACTIVATE_PENDING_ORGANIZER;
        message += "/:" + organizerUser.getEmail();

        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name), Api.APP_EMAIL,
                "" + getText(R.string.app_name), "" + getText(R.string.mail_subject_organizer_registering), message);
    }

}
