package com.jacksonsmolenko.iwmy.cooltools;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vk.sdk.VKSdk;

public class CoolApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        VKSdk.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static void writePreferences(String label, int message) {
        writePreferences(label, "" + message);
    }

    public static void writePreferences(String label, boolean message) {
        writePreferences(label, "" + message);
    }

    public static void writePreferences(String label, String message) {
        context.getSharedPreferences("", Context.MODE_PRIVATE).edit()
                .putString(label, message).commit();
    }

    public static int readPreferences(String label, int defaultValue) {
        return CoolFormatter.parseInt(readPreferences(label, "" + defaultValue), defaultValue);
    }

    public static boolean readPreferences(String label, boolean defaultValue) {
        return CoolFormatter.parseBoolean(readPreferences(label, "" + defaultValue), defaultValue);
    }

    public static String readPreferences(String label, String defaultValue) {
        return context.getSharedPreferences("", Context.MODE_PRIVATE)
                .getString(label, defaultValue);
    }

    public static Context getContext() {
        return context;
    }
}
