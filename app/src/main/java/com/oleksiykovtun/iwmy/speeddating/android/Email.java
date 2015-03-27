package com.oleksiykovtun.iwmy.speeddating.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;

/**
 * Created by alx on 2015-03-11.
 */
public class Email {

    private final static String EMAIL_ADDRESS = "iwmy.speed.dating@gmail.com";
    private final static String EMAIL_PASSWORD = "$p3eDd@t|nG";

    public static void sendOrganizerRequest(Context context, User organizerUser) {
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
        send(context, EMAIL_ADDRESS, "New organizer", message);
    }

    public static void send(Context context, String recipientEmail, String subject, String text) {
        Intent gMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", recipientEmail, null));
        gMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        gMailIntent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(Intent.createChooser(gMailIntent, "Send email..."));
        } catch (ActivityNotFoundException e) {
            Log.e("IWMY", "Email sending failed", e);
        }
    }

}
