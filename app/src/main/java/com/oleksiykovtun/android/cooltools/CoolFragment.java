package com.oleksiykovtun.android.cooltools;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
        CoolRecyclerAdapter.CoolClickListener, CoolWebAsyncTask.CoolWebAsyncResponse,
        CoolDatePickerFragment.DateSetListener, CoolTimePickerFragment.TimeSetListener {

    private View containerView;
    private CoolWebAsyncTask task = null;
    private Toast toast = null;
    private static CountDownTimer timer = null;

    public CoolFragment() {
    }

    protected boolean isRadioButtonChecked(int radioButtonId) {
        return ((RadioButton) getViewById(radioButtonId)).isChecked();
    }

    protected void openMenu(View view) {
        registerForContextMenu(view);
        getActivity().openContextMenu(view);
    }

    protected View getViewById(int viewId) {
        return containerView.findViewById(viewId);
    }

    protected void setButtonEnabled(int buttonId, boolean isEnabled) {
        ((Button) getViewById(buttonId)).setEnabled(isEnabled);
    }

    protected String getEditText(int editTextId) {
        return "" + ((EditText) getViewById(editTextId)).getText();
    }

    protected String getLabelText(int textViewId) {
        return "" + ((TextView) getViewById(textViewId)).getText();
    }

    protected void openDatePicker() {
        CoolDatePickerFragment datePickerFragment = new CoolDatePickerFragment();
        datePickerFragment.setDateSetListener(this);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    protected void openTimePicker() {
        CoolTimePickerFragment timePickerFragment = new CoolTimePickerFragment();
        timePickerFragment.setTimeSetListener(this);
        timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    protected long getDateMillis(int datePickerId) {
        DatePicker datePicker = (DatePicker) getViewById(datePickerId);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                0, 0, 0);
        return calendar.getTimeInMillis();
    }

    protected long getTimeMillis(int timePickerId) {
        TimePicker timePicker = (TimePicker) getViewById(timePickerId);
        return (timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()) * 60 * 1000;
    }

    protected void registerContainerView(View containerView) {
        setHasOptionsMenu(true);
        this.containerView = containerView;
    }

    protected void registerClickListener(int clickableId) {
        (getViewById(clickableId)).setOnClickListener(this);
    }

    protected void registerItemClickListener(CoolRecyclerAdapter recyclerAdapter) {
        recyclerAdapter.setOnItemClickListener(this);
    }

    protected ImageView getImageView(int imageViewId) {
        return  ((ImageView) getViewById(imageViewId));
    }

    private void showToast(String message, int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getActivity().getApplicationContext(), message, length);
        toast.show();
    }

    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected void showToast(int messageResourceId) {
        showToast(getString(messageResourceId));
    }

    protected void showToastLong(String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    protected void showToastLong(int messageResourceId) {
        showToastLong(getString(messageResourceId));
    }

    protected Serializable getAttachment() {
        return CoolFragmentManager.getAttachment(this);
    }

    protected void setText(int textViewId, String text) {
        try {
            ((TextView) getViewById(textViewId)).setText(text);
        } catch (Throwable e) {
            Log.e("IWMY", "No view found for text: " + text, e);
        }
    }

    protected void setText(int textViewId, int textId) {
        setText(textViewId, "" + getText(textId));
    }

    protected void setText(int textViewId, int textId, String text) {
        setText(textViewId, getText(textId) + " " + text);
    }

    protected void post(String host, String path, String authorizationId, String password,
                        String clientVersion, Class responseClass, Object... postData) {
        cancelPost();
        task = new CoolWebAsyncTask(path, authorizationId, password, clientVersion, this,
                responseClass, postData);
        task.execute(host + path);
    }

    protected boolean isPostRequestRunningNow() {
        return task != null && task.isRunningNow();
    }

    protected void cancelPost() {
        if (task != null) {
            task.cancel();
        }
    }

    protected void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    protected void startTimer() {
        stopTimer();
        timer = new CountDownTimer(3600000, 6000) {

            @Override
            public void onTick(long millisUntilFinished) {
                onTimerTick();
            }

            @Override
            public void onFinish() {
            }

        }.start();
    }

    protected void onTimerTick() {
    }

    @Override
    public void onPostReceiveObjectList(String tag, List responseObjectList) {
    }

    @Override
    public void onPostConnectionError() {
    }

    @Override
    public void onPostAuthorizationError() {
    }

    @Override
    public void onPostAccessError() {
    }

    @Override
    public void onPostVersionError() {
    }

    @Override
    public void onPostError() {
    }

    @Override
    public void onClick(View view) {
    }

    private void hideKeyboard() {
        if (getActivity()!= null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onDateSet(String dateString) {
    }

    @Override
    public void onTimeSet(String timeString) {
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
        stopTimer();
        cancelPost();
        hideKeyboard();
    }

}
