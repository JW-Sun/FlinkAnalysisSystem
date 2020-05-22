package com.jw.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd HH");
        }
    };

    public static Long getCurrentHourStart(Long visitTime) {
        SimpleDateFormat sdf = (SimpleDateFormat) threadLocal.get();
        Date date = new Date(visitTime);
        String format = sdf.format(date);
        try {
            Date parse = sdf.parse(format);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getCurrentDayStart(Long visitTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(visitTime);
        try {
            Date parse = sdf.parse(sdf.format(date));
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getCurrentMonthStart(Long visitTime) {
        Calendar cal =Calendar.getInstance();
        if (null != visitTime) {
            cal.setTimeInMillis(visitTime);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getCurrentWeekStart(Long visitTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long getCurrentFiveMinuteStart(Long visitTime) {
        Calendar cal =Calendar.getInstance();
        if (null != visitTime) {
            cal.setTimeInMillis(visitTime);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();

    }


    public static String getByInterMinute(String time) {
        return null;
    }
}
