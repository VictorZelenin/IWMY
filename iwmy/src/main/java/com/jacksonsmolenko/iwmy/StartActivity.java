package com.jacksonsmolenko.iwmy;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.TutorialFragment;
import com.vk.sdk.VKSdk;

import java.util.List;

public class StartActivity extends FragmentActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        CoolFragmentManager.setup(this, R.id.fragment_holder);

        if (! CoolFragmentManager.isNotEmpty()) {
            CoolFragmentManager.showAtBottom(new TutorialFragment()); // show the first fragment
        }
    }

    @Override
    public void onBackPressed() {
        if (CoolFragmentManager.isNotEmpty()) {
            CoolFragmentManager.showPrevious(); // pop the top fragment from the stack
        }
        // If there are no fragments in the stack to show, go back from the activity itself
        if (! CoolFragmentManager.isNotEmpty()) {
            super.onBackPressed();
        }
    }
}
