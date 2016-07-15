package com.jacksonsmolenko.iwmy;

import android.util.Log;

import com.google.gson.Gson;
import com.jacksonsmolenko.iwmy.cooltools.CoolApplication;
import com.oleksiykovtun.iwmy.speeddating.data.User;

public class Account {

    private static final String USER = "user";

    public static boolean hasUser() {
        return getUser().getEmail().contains("@") && getUser().getPassword().length() > 0;
    }

    public static void saveUser(Object object) {
        try {
            CoolApplication.writePreferences(USER, new Gson().toJson(object, User.class));
        } catch (Throwable e) {
            Log.e("IWMY", "User saving to settings failed.", e);
        }
        Log.d("IWMY", "User settings saved.");
    }

    public static User getUser() {
        // don't need to create new User instance here
        User user = null;
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

    public static void removeUser() {
        saveUser(new User());
    }

}
