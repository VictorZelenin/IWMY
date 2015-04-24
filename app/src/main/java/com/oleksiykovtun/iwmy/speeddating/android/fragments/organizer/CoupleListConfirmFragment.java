package com.oleksiykovtun.iwmy.speeddating.android.fragments.organizer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.CoupleRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by alx on 2015-02-12.
 */
public class CoupleListConfirmFragment extends AppFragment {

    private List<Couple> coupleList = new ArrayList<Couple>();
    private Event event = null;

    private CoupleRecyclerAdapter coupleRecyclerAdapter = new CoupleRecyclerAdapter(coupleList);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_couple_list_confirm, container,
                false);
        registerContainerView(view);
        registerClickListener(R.id.button_send_results);
        registerClickListener(R.id.button_settings);

        event = (Event) getAttachment();

        RecyclerView coupleRecyclerView = (RecyclerView) view.findViewById(R.id.couple_list_holder);
        coupleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(coupleRecyclerAdapter);
        coupleRecyclerView.setAdapter(coupleRecyclerAdapter);

        post(Api.COUPLES + Api.GENERATE_FOR_EVENT, Couple[].class, event);

        return view;
    }

    @Override
    public void onReceiveWebData(String postTag, List response) {
        switch (postTag) {
            case Api.COUPLES + Api.GENERATE_FOR_EVENT:
                coupleList.clear();
                coupleList.addAll(response);
                coupleRecyclerAdapter.notifyDataSetChanged();
                if (coupleList.size() == 1) {
                    setText(R.id.label_couple_list, R.string.label_new_couple_list_one);
                } else if (coupleList.size() > 1) {
                    setText(R.id.label_couple_list, R.string.label_new_couple_list_many);
                }
                break;
            case Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET:
                post(Api.COUPLES + Api.PUT, Couple[].class, coupleList.toArray());
                break;
            case Api.COUPLES + Api.PUT:
                post(Api.MAIL + Api.SEND, Email[].class, getCouplesConfirmationEmails().toArray());
                break;
            case Api.MAIL + Api.SEND:
                post(Api.EVENTS + Api.SET_UNACTUAL, Event[].class, event);
                break;
            case Api.EVENTS + Api.SET_UNACTUAL:
                showToast(R.string.message_couples_sent);
                CoolFragmentManager.showAtBottom(new CoupleListFragment(), event);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_results:
                post(Api.USERS + Api.GET_FOR_EVENT_ACTIVE_RESET, User[].class, event);
                break;
            case R.id.button_settings:
                CoolFragmentManager.showAtTop(new SettingsFragment());
                break;
        }
    }


    private List<Email> getCouplesConfirmationEmails() {
        List<Email> emails = new ArrayList<>();
        emails.add(getEmailForOrganizer());
        for (User user : getUniqueUsersFromCouples()) {
            Email userEmail = new Email(Api.APP_EMAIL, "" + getText(R.string.app_name),
                    user.getEmail(), user.getNameAndSurname(), "", "");
            String subject;
            String message;
            User[] coupleUsers = getCoupleUsersForUser(user);
            switch (coupleUsers.length) {
                case 0:
                    subject = "" + getText(R.string.mail_subject_user_thank_you);
                    message = "" + getText(R.string.mail_text_user_no_couples);
                    break;
                case 1:
                    subject = "" + getText(R.string.mail_subject_user_you_found);
                    message = "" + getText(R.string.mail_text_user_one_couple);
                    break;
                default:
                    subject = "" + getText(R.string.mail_subject_user_you_found);
                    message = "" + getText(R.string.mail_text_user_many_couples);
                    break;
            }
            message = message.replace("COUPLE_COUNT", "" + coupleUsers.length);
            message = message.replace("CONTACTS_ONE_COUPLE", getUserContactInfo(coupleUsers[0]));
            message = message.replace("CONTACTS_MANY_COUPLES", getUsersContactInfo(coupleUsers));
            message = message.replace("CONTACTS_ORGANIZER", event.getOrganizerEmail() + "\n"
                    + Account.getUser().getPhone() + "\n" + event.getStreetAddress());
            message = message.replace("CONTACTS_SPEED_DATING", Api.APP_EMAIL);
            userEmail.setSubject(subject);
            userEmail.setMessage(message);
            emails.add(userEmail);
        }
        return emails;
    }

    private String getUserContactInfo(User user) {
        return user.getNameAndSurname() + "\n" + user.getEmail() + "\n" + user.getPhone();
    }

    private String getUsersContactInfo(User[] users) {
        String contactInfo = "";
        if (users.length > 0) {
            for (int i = 0; i < users.length - 1; ++i) {
                contactInfo += getUserContactInfo(users[i]);
                contactInfo += "\n\n";
            }
            contactInfo += getUserContactInfo(users[users.length - 1]);
        } else {
            contactInfo += getText(R.string.label_empty);
        }
        return contactInfo;
    }

    private User getUser1FromCouple(Couple couple) {
        return new User(couple.getUserEmail1(), "", couple.getUsername1(), "",
                couple.getName1(), "", couple.getPhone1(), couple.getBirthDate1(), "",
                "", "", "", "", "", "", "", "", "", "", "");
    }

    private User getUser2FromCouple(Couple couple) {
        return new User(couple.getUserEmail2(), "", couple.getUsername2(), "",
                couple.getName2(), "", couple.getPhone2(), couple.getBirthDate2(), "",
                "", "", "", "", "", "", "", "", "", "", "");
    }

    private User[] getCoupleUsersForUser(User user) {
        Set<User> coupleUsers = new TreeSet<>();
        for (Couple couple : coupleList) {
            if (couple.getUserEmail2().equals(user.getEmail())) {
                coupleUsers.add(getUser1FromCouple(couple));
            } else if (couple.getUserEmail1().equals(user.getEmail())) {
                coupleUsers.add(getUser2FromCouple(couple));
            }
        }
        return coupleUsers.toArray(new User[coupleUsers.size()]);
    }

    private User[] getUniqueUsersFromCouples() {
        Set<User> uniqueUsers = new TreeSet<>();
        for (Couple couple : coupleList) {
            uniqueUsers.add(getUser1FromCouple(couple));
            uniqueUsers.add(getUser2FromCouple(couple));
        }
        return uniqueUsers.toArray(new User[uniqueUsers.size()]);
    }

    private Email getEmailForOrganizer() {
        String message = "" + getText(R.string.mail_text_organizer_event_successful);
        message = message.replace("COUPLE_COUNT", "" + coupleList.size());
        message = message.replace("CONTACTS_COUPLE_LIST", getCouplesContactInfo());
        return new Email(Api.APP_EMAIL, "" + getText(R.string.app_name),
                event.getOrganizerEmail(), event.getPlace(),
                "" + getText(R.string.mail_subject_organizer_event_successful),
                message);
    }

    private String getCoupleContactInfo(Couple couple) {
        return "" + getText(R.string.label_couple) + "\n\n"
                + couple.getName1() + "\n" + couple.getUserEmail1() + "\n" + couple.getPhone1()
                + "\n\n"
                + couple.getName2() + "\n" + couple.getUserEmail2() + "\n" + couple.getPhone2() ;
    }

    private String getCouplesContactInfo() {
        String contactInfo = "";
        if (coupleList.size() > 0) {
            for (int i = 0; i < coupleList.size() - 1; ++i) {
                contactInfo += getCoupleContactInfo(coupleList.get(i));
                contactInfo += "\n\n";
            }
            contactInfo += getCoupleContactInfo(coupleList.get(coupleList.size() - 1));
        } else {
            contactInfo += getText(R.string.label_empty);
        }
        return contactInfo;
    }

}
