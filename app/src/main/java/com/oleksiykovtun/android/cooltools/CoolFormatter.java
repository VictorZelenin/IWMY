package com.oleksiykovtun.android.cooltools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.net.PercentEscaper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by alx on 2015-03-06.
 */
public class CoolFormatter {

    public static String escapeUrl(String unescapedString) {
        return new PercentEscaper("", false).escape(unescapedString);
    }

    public static Bitmap getImageBitmap(String base64String) throws Throwable {
        byte[] imageData = Base64.decode(base64String.getBytes(Charsets.UTF_8), Base64.DEFAULT);
        Bitmap result = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        return result;
    }

    public static int parseInt(String string) {
        return parseInt(string, 0);
    }

    public static int parseInt(String string, int defaultValue) {
        int result = defaultValue;
        try {
            result =  Integer.parseInt(string);
        } catch (Exception e) {
            Log.d("IWMY", "Parsing error");
        }
        return result;
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

}
