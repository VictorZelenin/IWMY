package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public class RegisteringFragment extends CoolFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registering, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_register);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                String email = getEditText(R.id.input_email);
                String password = getEditText(R.id.input_password);
                String username = getEditText(R.id.input_username);
                String group = "user";
                String nameAndSurname = getEditText(R.id.input_name_and_surname);
                String photoBase64 = "";
                String phone = getEditText(R.id.input_phone);
                String birthDate = CoolFormatter.getDateTime(getDateMillis(R.id.date_picker));
                String gender = isRadioButtonChecked(R.id.gender_male) ? "male" : "female";
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

                User user = new User(email, password, username, group, nameAndSurname,
                        photoBase64, phone, birthDate, gender, orientation, goal, affair, height,
                        weight, attitudeToSmoking, attitudeToAlcohol, location, organization,
                        website);
                if (checkUser(user)) {
                    post("http://iwmy-speed-dating.appspot.com/users/add", User[].class, user);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    private boolean checkUser(User user) {
        return user.getEmail().contains("@")
                && ! user.getNameAndSurname().isEmpty()
                && ! user.getPassword().isEmpty()
                && ! user.getUsername().isEmpty()
                && CoolFormatter.isDateValid(user.getBirthDate());
    }

    @Override
    public void onReceiveWebData(List response) {
        if (! response.isEmpty()) {
            Account.saveUser(this, response.get(0));
            showToast(R.string.message_registered);
            CoolFragmentManager.switchToRootFragment(new EventListFragment());
        } else {
            showToastLong(R.string.message_connection_error);
        }
    }

}
