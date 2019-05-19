package com.company.project.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimeUtils {

    public static final long MILL = 1L;
    public static final long SECONDS = 1000 * MILL;
    public static final long MINUTE = 60 * SECONDS;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long YEAR = 365 * DAY;

    private static final String S_MILL = "毫秒";
    private static final String S_SECENDS = "秒";
    private static final String S_MINUTE = "分钟";
    private static final String S_HOUR = "小时";
    private static final String S_DAY = "天";
    private static final String S_LESS_SECENDS = "小于1秒";
    public static final String BEFORE = "前";

    private DateTimeUtils() {
    }

//    public static void main(String[] args) {
////        System.out.println(formatMDHmm(System.currentTimeMillis() - 86400 * 1000L));
////        System.out.println(formatY_M_dHmm(System.currentTimeMillis() - 86400 * 1000L));
////        getLastWeekDays();
//        System.out.println(fmtYMDhmss(System.currentTimeMillis()));
//    }

    public static String[] getLastWeekDays() {
        String[] days = new String[7];
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        long todayZero = calendar.getTimeInMillis();
        System.out.println(todayZero);
        System.out.println(formatY_M_dHmm(todayZero));
        for (int i = 0; i < days.length; i++) {

        }

        return days;
    }

    /**
     * 根据上下线状态及时间创建对应文字说明
     */
    public static String createTimeInfoByStatusLength(boolean onlineStatus, long mills) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf_mdhmm = new SimpleDateFormat("M月d日 H:mm");
        String status = "";
        String time = "";
        long lll = System.currentTimeMillis() - mills;
        if (lll < MINUTE) {
            status = onlineStatus ? "上线 " : "离线 ";
            time = (lll / SECONDS) + S_SECENDS + BEFORE;
        } else if (lll < HOUR) {
            status = onlineStatus ? "上线 " : "离线 ";
            time = (lll / MINUTE) + S_MINUTE + BEFORE;
        } else if (lll < DAY) {
            status = onlineStatus ? "上线 " : "离线 ";
            time = (lll / HOUR) + S_HOUR + BEFORE;
        } else {
            status = onlineStatus ? "上线时间:" : "离线时间:";
            GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
            calendar.setTimeInMillis(mills);
            time = sdf_mdhmm.format(calendar.getTime());
        }
        return status + time;
    }

    /**
     * 格式化时间
     *
     * @param mills 时间毫秒值
     * @return MD_Hmm格式时间
     */
    public static String formatMDHmm(long mills) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfMdHmm = new SimpleDateFormat("M月d日 H:mm");
        GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.setTimeInMillis(mills);
        return sdfMdHmm.format(calendar.getTime());
    }

    /**
     * 格式化时间
     *
     * @param mills 时间毫秒值
     * @return MD_Hmm格式时间
     */
    public static String formatYmd(long mills) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfMdHmm = new SimpleDateFormat("yyyy年M月d日");
        GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.setTimeInMillis(mills);
        return sdfMdHmm.format(calendar.getTime());
    }

    /**
     * 格式化时间
     *
     * @param mills 时间毫秒值
     * @return MD_Hmm格式时间
     */
    public static String formatYMD4Gallery(long mills) {
        if (mills < DAY * 365) {
            mills *= 1000;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfMdHmm = new SimpleDateFormat("yyyy年M月d日");
        GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.setTimeInMillis(mills);
        return sdfMdHmm.format(calendar.getTime());
    }


    /**
     * 格式化时间
     *
     * @param mills 时间毫秒值
     * @return y-M-D_Hmm格式时间
     */
    public static String formatY_M_dHmm(long mills) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfy_M_dHmm = new SimpleDateFormat("yy-M-d H:mm");
        GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.setTimeInMillis(mills);
        return sdfy_M_dHmm.format(calendar.getTime());
    }

    /**
     * 返回当前时间戳
     */
    public static String fmtYMDhmssNow() {
        return fmtYMDhmss(System.currentTimeMillis());
    }


    /**
     * 获取时间戳格式字符
     */
    public static String fmtYMDhmss(long mills) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat yyMMddhhmmssSS = new SimpleDateFormat("yyMMddHHmmssSS");
        GregorianCalendar calendar = new GregorianCalendar(Locale.CHINA);
        calendar.setTimeInMillis(mills);
        return yyMMddhhmmssSS.format(calendar.getTime());
    }

    /**
     * 将毫秒值转换为时长（仅保留最大单位）
     */
    public static String mill2Time(long mill) {
        String time;
        if (mill < SECONDS) {
            time = S_LESS_SECENDS;
        } else if (mill < MINUTE) {
            time = mill / SECONDS + S_SECENDS;
        } else if (mill < HOUR) {
            time = mill / MINUTE + S_MINUTE;
        } else if (mill < DAY) {
            time = mill / HOUR + S_HOUR;
        } else {
            time = mill / DAY + S_DAY;
        }
        return time;
    }

    /**
     * 将毫秒值转换为时长（从大到小保留3个单位）
     */
    public static String mill2FullTime(long mill) {
        String time;
        if (mill < SECONDS) {
            time = S_LESS_SECENDS;
        } else if (mill < MINUTE) {
            time = mill / SECONDS + S_SECENDS;
        } else if (mill < HOUR) {
            long sec = (mill % MINUTE) / SECONDS;
            // x分钟(y>0)秒
            time = mill / MINUTE + S_MINUTE + (sec > 0 ? sec + S_SECENDS : "");
        } else if (mill < DAY) {
            long minute = (mill % HOUR) / MINUTE;
            long sece = ((mill % HOUR) % MINUTE) / SECONDS;
            // x小时(y=0分钟z>0秒) 或 x小时(y>0分钟) 或 x小时
            time = mill / HOUR + S_HOUR + (sece > 0 ? minute + S_MINUTE + sece + S_SECENDS : (minute > 0 ? minute + S_MINUTE : ""));
        } else {
            long hour = (mill % DAY) / HOUR;
            long mnt = ((mill % DAY) % HOUR) / MINUTE;
            time = mill / DAY + S_DAY + (mnt > 0 ? hour + S_HOUR + mnt + S_MINUTE : (hour > 0 ? hour + S_HOUR : ""));
        }
        return time;

    }

}
