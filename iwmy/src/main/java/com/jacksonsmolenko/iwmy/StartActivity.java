package com.jacksonsmolenko.iwmy;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.TutorialFragment;

import java.util.List;

public class StartActivity extends FragmentActivity {

    List<View> pages = null;

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
