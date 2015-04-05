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
        CoolApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return CoolApplication.context;
    }

}
