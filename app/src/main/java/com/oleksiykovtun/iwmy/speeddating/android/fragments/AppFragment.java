package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.android.Account;


/**
 * Created by alx on 2015-02-04.
 */
public abstract class AppFragment extends CoolFragment {

    protected void post(String path, Class responseClass, Object... postData) {
        post(BuildConfig.BACKEND_URL, path, Account.getUser().getEmail(),
                Account.getUser().getPassword(), responseClass, postData);
    }

}
