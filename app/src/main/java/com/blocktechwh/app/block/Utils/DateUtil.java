package com.blocktechwh.app.block.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date parseDate(String dateStr) {
        if (-1 == dateStr.indexOf("-")) {
            return parseDate(dateStr, "yyyyMMdd");
        } else {
            return parseDate(dateStr, "yyyy-MM-dd");
        }
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatDatetime(Date time) {
        return formatDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date offsetDate(Date date, int offset) {
        return offsetDate(date, offset, Calendar.DAY_OF_YEAR);
    }

    public static Date offsetDate(Date date, int offset, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, offset);
        return calendar.getTime();
    }

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long betweenDays = Math.abs(time2 - time1) / (1000 * 3600 * 24) + 1;
        return (int) betweenDays;
    }

    public static Date clearSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date clearField(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(field, 0);
        return cal.getTime();
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
        System.out.println(DateUtil.clearField(new Date(), Calendar.SECOND).getTime());
        System.out.println(DateUtil.clearSecond(new Date()).getTime());
        System.out.println(DateUtil.formatDate(new Date()));
        System.out.println(DateUtil.formatDate(DateUtil.clearSecond(new Date())));
        System.out.println(DateUtil.formatDate(DateUtil.offsetDate(new Date(), -5)));
        System.out.println(DateUtil.formatDate(DateUtil.offsetDate(DateUtil.parseDate("2015-12-28"), 5)));
    }

}
