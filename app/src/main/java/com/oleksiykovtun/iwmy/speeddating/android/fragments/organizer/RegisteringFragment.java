package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class RegisteringFragment extends CoolFragment {

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
                String group = User.ORGANIZER;
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

                User user = new User(email, password, username, group, nameAndSurname,
                        photoBase64, phone, birthDate, gender, orientation, goal, affair, height,
                        weight, attitudeToSmoking, attitudeToAlcohol, location, organization,
                        website);
                if (checkUser(user)) {
                    post(Api.MAIL + Api.SEND, Email[].class, getOrganizerRequestEmail(user));
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    @Override
    public void onReceiveWebData(List unsentEmails) {
        if (unsentEmails.isEmpty()) {
            CoolFragmentManager.showAtBottom(new AppliedFragment());
        } else {
            showToastLong(R.string.message_connection_error);
        }
    }

    private boolean checkUser(User user) {
        return user.getEmail().contains("@")
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
        message += "\nPassword: " + organizerUser.getPassword();
        message += "\nUsername: " + organizerUser.getUsername();
        message += "\nNameAndSurname: " + organizerUser.getNameAndSurname();
        message += "\nPhone: " + organizerUser.getPhone();
        message += "\nOrganization: " + organizerUser.getOrganization();
        message += "\nWebsite: " + organizerUser.getWebsite() + "\n\n";

        message += "Follow the link to add the organizer:\n";
        message += Api.BACKEND_URL + Api.USERS + Api.DEBUG_ADD_ORGANIZER;
        message += "/email=" + CoolFormatter.escapeUrl(organizerUser.getEmail());
        message += "&password=" + CoolFormatter.escapeUrl(organizerUser.getPassword());
        message += "&username=" + CoolFormatter.escapeUrl(organizerUser.getUsername());
        message += "&name=" + CoolFormatter.escapeUrl(organizerUser.getNameAndSurname());
        message += "&phone=" + CoolFormatter.escapeUrl(organizerUser.getPhone());
        message += "&organization=" + CoolFormatter.escapeUrl(organizerUser.getOrganization());
        message += "&website=" + CoolFormatter.escapeUrl(organizerUser.getWebsite());

        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name), Api.APP_EMAIL,
                "" + getText(R.string.app_name), "" + getText(R.string.mail_organizer_registering), message);
    }

}
