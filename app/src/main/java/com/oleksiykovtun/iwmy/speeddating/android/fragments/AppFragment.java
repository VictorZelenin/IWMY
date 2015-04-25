package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;


/**
 * Created by alx on 2015-02-04.
 */
public abstract class AppFragment extends CoolFragment {

    protected void post(String path, Class responseClass, Object... postData) {
        post(BuildConfig.BACKEND_URL, path, Account.getUser().getEmail(),
                Account.getUser().getPassword(), BuildConfig.VERSION_NAME, responseClass, postData);
    }

    @Override
    public void onPostConnectionError() {
        showToastLong(R.string.message_connection_error);
    }

    @Override
    public void onPostAuthorizationError() {
        showToastLong(R.string.message_authorization_error);
    }

    @Override
    public void onPostAccessError() {
        showToastLong(R.string.message_access_error);
    }

    @Override
    public void onPostVersionError() {
        showToastLong(R.string.message_version_error);
    }

    @Override
    public void onPostError() {
        showToastLong(R.string.message_other_post_error);
    }

}
