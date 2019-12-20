package com.jwong.education.util;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {

    public static Date convert2Date(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            Log.e("DateFormatUtil", "转日期出错了", e);
        }
        return null;
    }

    public static Date convert2DateTime(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            Log.e("DateFormatUtil", "转时间出错了", e);
        }
        return null;
    }


    public static String convert2DateTime(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            Log.e("DateFormatUtil", "转时间出错了", e);
        }
        return "";
    }

    public static String convert2Date(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            Log.e("DateFormatUtil", "转日期出错了", e);
        }
        return "";
    }

    public static String priceFormat(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}