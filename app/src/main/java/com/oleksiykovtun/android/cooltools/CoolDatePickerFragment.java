package com.oleksiykovtun.android.cooltools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by alx on 2015-03-13.
 */
public class CoolDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String dateFormat = "yyyy-MM-dd";
    private DateSetListener delegate;

    public interface DateSetListener {
        public void onDateSet(String dateString);
    }

    public void setDateSetListener(DateSetListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        try {
            delegate.onDateSet(new SimpleDateFormat(dateFormat).format(calendar.getTimeInMillis()));
        } catch (Throwable e) {
            Log.e("IWMY", "Date formatting exception", e);
        }
    }
}