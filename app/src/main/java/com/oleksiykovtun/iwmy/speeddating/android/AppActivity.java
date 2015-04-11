package com.oleksiykovtun.iwmy.speeddating.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;
import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.android.cooltools.CoolFragmentManager;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.StartFragment;

import io.fabric.sdk.android.Fabric;

/**
 * The only activity in the app
 */
public class AppActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity);

        CoolFragmentManager.setup(this, R.id.fragment_holder);

        if (! CoolFragmentManager.isNotEmpty()) {
            CoolFragment.setUrlPrefix(Api.BACKEND_URL);
            CoolFragmentManager.showAtBottom(new StartFragment()); // show the first fragment
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
