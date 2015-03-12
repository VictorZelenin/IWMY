package com.oleksiykovtun.android.cooltools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;


/**
 * Base fragment with support of clickable elements and recycler items clicks processing
 */
public abstract class CoolFragment extends Fragment implements View.OnClickListener,
        CoolRecyclerAdapter.CoolClickListener, CoolWebAsyncTask.CoolWebAsyncResponse {

    private View containerView;
    private AsyncTask task = null;

    public CoolFragment() { }

    protected boolean isRadioButtonChecked(int radioButtonId) {
        return ((RadioButton)containerView.findViewById(radioButtonId)).isChecked();
    }

    protected String getEditText(int editTextId) {
        return "" + ((EditText)containerView.findViewById(editTextId)).getText();
    }

    protected long getDateMillis(int datePickerId) {
        DatePicker datePicker = (DatePicker)containerView.findViewById(datePickerId);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                0, 0, 0);
        return calendar.getTimeInMillis();
    }

    protected long getTimeMillis(int timePickerId) {
        TimePicker timePicker = (TimePicker)containerView.findViewById(timePickerId);
        return (timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()) * 60 * 1000;
    }

    protected void registerContainerView(View containerView) {
        setHasOptionsMenu(true);
        this.containerView = containerView;
    }

    protected void registerClickListener(int clickableId) {
        (containerView.findViewById(clickableId)).setOnClickListener(this);
    }

    protected void registerItemClickListener(CoolRecyclerAdapter recyclerAdapter) {
        recyclerAdapter.setOnItemClickListener(this);
    }

    protected void setImageFromBase64String(int imageViewId, String base64String) {
        if (! base64String.isEmpty()) {
            try {
                ((ImageView) containerView.findViewById(imageViewId))
                        .setImageBitmap(CoolFormatter.getImageBitmap(base64String));
            } catch (Throwable e) {
                Log.e("IWMY", "Image setting failed", e);
            }
        }
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int messageResourceId) {
        showToast(getString(messageResourceId));
    }

    protected void showToastLong(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void showToastLong(int messageResourceId) {
        showToastLong(getString(messageResourceId));
    }

    protected Serializable getAttachment() {
        return CoolFragmentManager.getAttachment(this);
    }

    protected void setText(int textViewId, String text) {
        try {
            ((TextView) containerView.findViewById(textViewId)).setText(text);
        } catch (Throwable e) {
            Log.e("IWMY", "No view found for text: " + text, e);
        }
    }

    protected void setText(int textViewId, int textId, String text) {
        setText(textViewId, getText(textId) + " " + text);
    }

    protected void post(String url, Class responseClass, Object... postData) {
        cancelPost();
        task = new CoolWebAsyncTask(this, responseClass, postData).execute(url);
    }

    protected void cancelPost() {
        if (task != null && ! task.isCancelled()) {
            task.cancel(true);
        }
    }

    protected void post(String url, Object... postData) {
        new CoolWebAsyncTask(this, null, postData).execute(url);
    }

    @Override
    public void onReceiveWebData(List webDataErrorString) {
    }

    @Override
    public void onFailReceivingWebData(String webDataErrorString) {
        String messageConnectionError =
            "Connection error! Please check your internet connection and try again.";
        showToastLong(messageConnectionError);
    }

    @Override
    public void onClick(View view) {
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked) {
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
    }

    @Override
    public void onLongClick(Serializable objectAtClicked) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelPost();
        hideKeyboard();
    }

}
