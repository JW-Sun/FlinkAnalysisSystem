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

    // 得到的 0 5 10 分钟的概念。
    public static Long getCurrentFiveMinuteInterStart(Long time) {
        String timeString = getByInterMinute(time + "");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmm");
        try {
            Date date = dateFormat.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static String getByInterMinute(String time) {
        Long timeMillions = Long.valueOf(time);
        Date date = new Date(timeMillions);
        // 转成分钟？
        DateFormat dateFormatMinute = new SimpleDateFormat("mm");
        DateFormat dateFormatHour = new SimpleDateFormat("yyyyMMddHH");

        String minute = dateFormatMinute.format(date);
        String hour = dateFormatHour.format(date);

        Long minuteLong = Long.valueOf(minute);

        String replaceMinute = "";
        if (minuteLong >= 0 && minuteLong < 5) {
            replaceMinute = "00";
        } else if (minuteLong >= 5 && minuteLong < 10) {
            replaceMinute = "05";
        } else if (minuteLong >= 10 && minuteLong < 15) {
            replaceMinute = "10";
        } else if (minuteLong >= 15 && minuteLong < 20) {
            replaceMinute = "15";
        } else if (minuteLong >= 20 && minuteLong < 25) {
            replaceMinute = "20";
        } else if (minuteLong >= 25 && minuteLong < 30) {
            replaceMinute = "25";
        } else if (minuteLong >= 30 && minuteLong < 35) {
            replaceMinute = "30";
        } else if (minuteLong >= 35 && minuteLong < 40) {
            replaceMinute = "35";
        } else if (minuteLong >= 40 && minuteLong < 45) {
            replaceMinute = "40";
        } else if (minuteLong >= 45 && minuteLong < 50) {
            replaceMinute = "45";
        } else if (minuteLong >= 50 && minuteLong < 55) {
            replaceMinute = "50";
        } else if (minuteLong >= 55 && minuteLong < 60) {
            replaceMinute = "55";
        }

        String fullTime = hour + replaceMinute;


        return fullTime;
    }

    public static String getByInterHour(String time) {
        DateFormat dateFormatHour = new SimpleDateFormat("yyyyMMddHH");
        Long timeMillion = Long.valueOf(time);
        Date date = new Date(timeMillion);
        String hour = dateFormatHour.format(date);
        return hour;
    }

    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date date = dateFormat.parse("20200412 123223");
        String byInterMinute = getByInterMinute(date.getTime() + "");
        System.out.println(byInterMinute);

        String byInterHour = getByInterHour(date.getTime() + "");
        System.out.println(byInterHour);
    }
}
