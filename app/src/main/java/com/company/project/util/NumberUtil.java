package com.company.project.util;

import android.text.TextUtils;

import androidx.annotation.Size;

import java.text.DecimalFormat;

public class NumberUtil {

    /**
     * 金额格式化
     */
    public static String formatMoney(String str) {
        if ("0".equals(str) || "0.00".equals(str)) {
            return str;
        }
        Double cny = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        String CNY = df.format(cny);
        Double cnyd = Double.parseDouble(CNY);
        DecimalFormat df2 = new DecimalFormat("#,###.00");
        return df2.format(cnyd);
    }

    /**
     * 格式化资产
     */
    public static String formatAssets(String value) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        String str;
        float fv = 0f;
        try {
            fv = Float.parseFloat(value);
        } catch (Exception ignored) {

        }
        if (fv >= 100000.0f || fv <= -100000.0f) {
            str = format.format(fv / 10000.0f) + "万";
        } else {
            str = format.format(fv);
        }
        return str;
    }

    /**
     * 格式化资产
     */
    public static String formatAssets2(String value) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        String str;
        float fv = 0f;
        try {
            fv = Float.parseFloat(value);
        } catch (Exception ignored) {

        }
        if (fv >= 100000000.0f || fv <= -100000000.0f) {
            str = format.format(fv / 100000000.0f) + "亿";
        } else if (fv >= 100000.0f || fv <= -100000.0f) {
            str = format.format(fv / 10000.0f) + "万";
        } else {
            str = value;
        }
        return str;
    }

    /**
     * 隐藏手机号中间四位
     */
    public static String hideMobile(String mobile) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mobile) && mobile.length() > 6) {

            for (int i = 0; i < mobile.length(); i++) {
                char c = mobile.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

        }
        return sb.toString();
    }

    /**
     * 十进制数转16进制字符
     * decimal to hex
     *
     * @param number 十进制数
     * @return StringBuilder - 十六进制字符
     */
    private static StringBuilder d2h(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder;
    }

    /**
     * 十进制数转16进制字符
     * decimal to hex
     *
     * @param number 十进制数
     * @return 十六进制字符
     */
    public static String d2hex(int number) {
        return d2h(number).toString().toUpperCase();
    }

    /**
     * 十进制数转16进制字符
     * decimal to hex
     *
     * @param numbers 批量十进制数
     * @return 十六进制字符
     */
    public static String d2hex(int... numbers) {
        return d2hex("", numbers);
    }

    /**
     * 十进制数转16进制字符
     * decimal to hex
     *
     * @param numbers 批量十进制数
     * @return 十六进制字符
     */
    public static String d2hex(String start, int... numbers) {
        StringBuilder builder = new StringBuilder(start);
        for (int number : numbers) {
            builder.append(d2hex(number));
        }
        return builder.toString().toUpperCase();
    }

    /**
     * ARGB/RGB数字转16进制色值字符
     * color (r,g,b) decimal to hex
     *
     * @param numbers argb(a,r,g,b) / rgb(r,g,b)
     * @return 十六进制字符
     */
    public static String color(@Size(min = 3, max = 4) int... numbers) {
        return d2hex("#", numbers);
    }
}
