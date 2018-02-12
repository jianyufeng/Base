package com.android.base.util;

import com.fpi.mobile.utils.NumberUtil;
import com.fpi.mobile.utils.StringTool;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间数据提供工具类
 */
public class TimeUtils {
    public static final String yyyy = "yyyy";
    public static final String MM = "MM";
    /**
     * 根据格式获取  获取 long
     *
     * @param time
     * @return
     */
    public static long getLongTime(String time, String pattern) {
        if (StringTool.isEmpty(time)) return System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(time, pos);
        return date.getTime();
    }
    /**
     * 根据格式获取  获取 long
     *
     * @param time
     * @return
     */
    public static String getStringTime(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(time);
    }
    /**
     * 日增减
     */
    public static long dayChange(long currentDay, int change) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentDay));
        calendar.add(Calendar.DAY_OF_MONTH, change);
        return calendar.getTime().getTime();
    }
    /**
     * @param a
     * @param b return  0 a=b;-1 a<b ; 1 a>b;
     */
    public static int compareMonth(long a, long b) {
        int aY = NumberUtil.parseInteger(TimeUtils.getStringTime(a, yyyy));
        int aM = NumberUtil.parseInteger(TimeUtils.getStringTime(a, MM));
        int bY = NumberUtil.parseInteger(TimeUtils.getStringTime(b, yyyy));
        int bM = NumberUtil.parseInteger(TimeUtils.getStringTime(b, MM));
        if (aY < bY) {
            return -1;
        } else if (aY == bY) {
            if (aM < bM) {
                return -1;
            } else if (aM == bM) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
