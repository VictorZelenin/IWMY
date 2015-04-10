package com.oleksiykovtun.android.cooltools;

import android.app.Application;
import android.content.Context;

/**
 * Created by alx on 2015-04-05.
 */
public class CoolApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static void writePreferences(String label, int message) {
        writePreferences(label, "" + message);
    }

    public static void writePreferences(String label, String message) {
        context.getSharedPreferences("", Context.MODE_PRIVATE).edit()
                .putString(label, message).commit();
    }

    public static int readPreferences(String label, int defaultValue) {
        return CoolFormatter.parseInt(readPreferences(label, "" + defaultValue), defaultValue);
    }

    public static String readPreferences(String label, String defaultValue) {
        return context.getSharedPreferences("", Context.MODE_PRIVATE)
                .getString(label, defaultValue);
    }

    public static Context getContext() {
        return context;
    }

}
