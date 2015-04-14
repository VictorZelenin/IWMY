package com.oleksiykovtun.iwmy.speeddating.android.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
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
        registerClickListener(R.id.button_select_date);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_register:
                String email = getEditText(R.id.input_email);
                String password = getEditText(R.id.input_password);
                String username = getEditText(R.id.input_username);
                String group = User.USER;
                String nameAndSurname = getEditText(R.id.input_name_and_surname);
                String photoBase64 = "";
                String phone = getEditText(R.id.input_phone);
                String birthDate = getLabelText(R.id.label_birth_date);
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

                User user = new User(email, password, username, group, nameAndSurname,
                        photoBase64, phone, birthDate, gender, orientation, goal, affair, height,
                        weight, attitudeToSmoking, attitudeToAlcohol, location, organization,
                        website);
                if (checkUser(user)) {
                    post(Api.USERS + Api.ADD, User[].class, user);
                } else {
                    showToast(R.string.message_inputs_error);
                }
                break;
        }
    }

    private boolean checkUser(User user) {
        return user.getEmail().contains("@")
                && !user.getNameAndSurname().isEmpty()
                && !user.getPassword().isEmpty()
                && !user.getUsername().isEmpty()
                && CoolFormatter.isDateValid(user.getBirthDate());
    }

    @Override
    public void onReceiveWebData(List response) {
        if (!response.isEmpty()) {
            Account.saveUser(this, response.get(0));
            showToast(R.string.message_registered);
            CoolFragmentManager.showAtBottom(new EventListFragment());
        } else {
            showToastLong(R.string.message_user_exists);
        }
    }

    @Override
    public void onDateSet(String dateString) {
        setText(R.id.label_birth_date, dateString);
    }

}
