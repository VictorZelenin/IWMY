package com.oleksiykovtun.android.cooltools;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by alx on 2015-03-13.
 */
public class CoolTimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private String timeFormat = "HH:mm";
    private TimeSetListener delegate;

    public interface TimeSetListener {
        public void onTimeSet(String timeString);
    }

    public void setTimeSetListener(TimeSetListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hourOfDay, minute);
        try {
            delegate.onTimeSet(new SimpleDateFormat(timeFormat).format(calendar.getTimeInMillis()));
        } catch (Throwable e) {
            Log.e("IWMY", "Time formatting exception", e);
        }
    }

}