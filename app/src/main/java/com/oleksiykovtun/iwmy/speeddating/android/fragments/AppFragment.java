package com.oleksiykovtun.iwmy.speeddating.android.fragments;

import android.view.View;

import com.oleksiykovtun.android.cooltools.CoolFragment;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * App-specific, fragment-invariant features
 */
public abstract class AppFragment extends CoolFragment {

    protected void post(String path, Class responseClass, Object... postData) {
        post(BuildConfig.BACKEND_URL, path, Account.getUser().getEmail(),
                Account.getUser().getPassword(), BuildConfig.VERSION_NAME, responseClass, postData);
        startUpdatingProgressBar();
    }

    public void onPostReceive(String tag, List responseObjectList) {
    }

    public void onPostReceive(List responseObjectList) {
    }

    @Override
    public final void onPostReceiveObjectList(String tag, List responseObjectList) {
        onPostReceive(tag, responseObjectList);
        onPostReceive(responseObjectList);
        stopUpdatingProgressBar();
    }

    @Override
    public final void onPostConnectionError() {
        showToastLong(R.string.message_connection_error);
        stopUpdatingProgressBar();
    }

    @Override
    public final void onPostAuthorizationError() {
        showToastLong(R.string.message_authorization_error);
        stopUpdatingProgressBar();
    }

    @Override
    public final void onPostAccessError() {
        showToastLong(R.string.message_access_error);
        stopUpdatingProgressBar();
    }

    @Override
    public final void onPostVersionError() {
        showToastLong(R.string.message_version_error);
        stopUpdatingProgressBar();
    }

    @Override
    public final void onPostError() {
        showToastLong(R.string.message_other_post_error);
        stopUpdatingProgressBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopUpdatingProgressBar();
        hideUpdatingProgressBar();
    }


    private void startUpdatingProgressBar() {
        if (getUpdatingProgressBar() != null) {
            getUpdatingProgressBar().setVisibility(View.VISIBLE);
            getUpdatingProgressBar().progressiveStart();
        }
    }

    private void stopUpdatingProgressBar() {
        if (getUpdatingProgressBar() != null) {
            getUpdatingProgressBar().progressiveStop();
        }
    }

    private void hideUpdatingProgressBar() {
        if (getUpdatingProgressBar() != null) {
            getUpdatingProgressBar().setVisibility(View.INVISIBLE);
        }
    }

    private SmoothProgressBar getUpdatingProgressBar() {
        return (SmoothProgressBar) getViewById(R.id.progress_bar);
    }

}
