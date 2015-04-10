package com.oleksiykovtun.iwmy.speeddating.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.iwmy.speeddating.data.User;

/**
 * Created by alx on 2015-03-05.
 */
public class Account {

    private static final String USER = "user";

    public static boolean hasUser(Fragment fragment) {
        return getUser(fragment).getEmail().contains("@")
                && getUser(fragment).getPassword().length() > 0;
    }

    public static void saveUser(Fragment fragment, Object object) {
        try {
            CoolApplication.writePreferences(USER, new Gson().toJson(object, User.class));
        } catch (Throwable e) {
            Log.e("IWMY", "User saving to settings failed.", e);
        }
        Log.d("IWMY", "User settings saved.");
    }

    public static User getUser(Fragment fragment) {
        User user = new User();
        try {
            user = new Gson().fromJson(CoolApplication.readPreferences(USER, ""), User.class);
        } catch (Throwable e) {
            Log.e("IWMY", "User getting from settings failed.", e);
        }
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void removeUser(Fragment fragment) {
        saveUser(fragment, new User());
    }

}
