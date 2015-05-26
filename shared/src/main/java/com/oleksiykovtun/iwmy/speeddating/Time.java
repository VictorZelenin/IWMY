package com.oleksiykovtun.iwmy.speeddating;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by alx on 2015-03-31.
 */
public class Time {

    public static long getMillisFromDateTime(String dateTimeString) {
        final String dateTimeFormat = "yyyy-MM-dd HH:mm";
        long unixTimeOfDate;
        try {
            unixTimeOfDate = new SimpleDateFormat(dateTimeFormat).parse(dateTimeString).getTime();
        } catch (ParseException e) {
            return 0;
        }
        return System.currentTimeMillis() - unixTimeOfDate;
    }

    public static String getYearsFromDate(String dateString) {
        final String dateFormat = "yyyy-MM-dd";
        long unixTimeOfDate;
        try {
            unixTimeOfDate = new SimpleDateFormat(dateFormat).parse(dateString).getTime();
        } catch (ParseException e) {
            return "-";
        }
        long unixTimeElapsed = System.currentTimeMillis() - unixTimeOfDate;
        long value = -1;
        if (unixTimeElapsed > 0) {
            value = unixTimeElapsed / 1000 / 60 / 60 / 24 / 365;
        }
        return "" + value;
    }

    public static String getFullDateTimeNow() {
        final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        return new SimpleDateFormat(dateTimeFormat).format(System.currentTimeMillis());
    }
}
