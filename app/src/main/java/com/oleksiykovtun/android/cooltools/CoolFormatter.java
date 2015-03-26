package com.oleksiykovtun.android.cooltools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.common.base.Charsets;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by alx on 2015-03-06.
 */
public class CoolFormatter {

    public static Bitmap getImageBitmap(String base64String) throws Throwable {
        byte[] imageData = Base64.decode(base64String.getBytes(Charsets.UTF_8), Base64.DEFAULT);
        Bitmap result = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        return result;
    }

    public static String getTime(long timeMilliseconds) {
        final String dateFormat = "yyyy-MM-dd";
        return getDateTime(timeMilliseconds, dateFormat);
    }

    public static String getDate(long timeMilliseconds) {
        final String dateFormat = "yyyy-MM-dd";
        return getDateTime(timeMilliseconds, dateFormat);
    }

    private static String getDateTime(long timeMilliseconds, String dateTimeFormat) {
        String value = "-";
        try {
            value = new SimpleDateFormat(dateTimeFormat).format(timeMilliseconds);
        } catch (Throwable e) {
            Log.e("IWMY", "Time formatting exception", e);
        }
        return value;
    }

    private static boolean isDateOrTimeValid(String dateString, String formatString) {
        boolean value = true;
        try {
            new SimpleDateFormat(formatString).parse(dateString).getTime();
        } catch (ParseException e) {
            value = false;
            Log.d("IWMY", "Date or time format exception for format " + formatString, e);
        }
        return value;
    }

    public static boolean isDateValid(String dateString) {
        return isDateOrTimeValid(dateString, "yyyy-MM-dd");
    }

    public static boolean isDateTimeValid(String dateTimeString) {
        return isDateOrTimeValid(dateTimeString, "yyyy-MM-dd HH:mm");
    }

    public static boolean isDateTimeFuture(String dateString) {
        final String dateFormat = "yyyy-MM-dd HH:mm";
        boolean value = false;
        try {
            long unixTimeOfDate = new SimpleDateFormat(dateFormat).parse(dateString).getTime();
            value = (unixTimeOfDate - System.currentTimeMillis() > 0);
        } catch (ParseException e) {
            Log.d("IWMY", "Time format exception", e);
        }
        return value;
    }

    public static String getYearsFromDate(String dateString) {
        final String dateFormat = "yyyy-MM-dd";
        long value = -1;
        try {
            long unixTimeOfDate = new SimpleDateFormat(dateFormat).parse(dateString).getTime();
            long unixTimeElapsed = System.currentTimeMillis() - unixTimeOfDate;
            if (unixTimeElapsed > 0) {
                value = unixTimeElapsed / 1000 / 60 / 60 / 24 / 365;
            }
        } catch (Throwable e) {
            Log.e("IWMY", "Date formatting exception", e);
        }
        return "" + value;
    }

}
