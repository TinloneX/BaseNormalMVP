package com.company.project.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import de.mateware.snacky.Snacky;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class Tips {

    /**
     * toast方法  显示在屏幕顶部
     *
     * @param mContext 上下文
     * @param msg      通知信息
     * @param distance 与顶部距离（dip）
     */
    public static void toastTop(Context mContext, String msg, int distance) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            int topDistance = DensityUtil.dip2px(mContext, distance);
            //左右padding
            int left = DensityUtil.dip2px(mContext, 15);
            //上下padding
            int top = DensityUtil.dip2px(mContext, 6);
            toast.setGravity(Gravity.TOP, 0, topDistance);
            TextView tv = new TextView(mContext.getApplicationContext());
            tv.setPadding(left, top, left, top);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tv.setText(msg);
            toast.setView(tv);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * toast方法  显示在屏幕中央
     *
     * @param mContext 上下文
     * @param msg      通知信息
     */
    public static void toastCenter(Context mContext, String msg) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            //左右padding
            int left = DensityUtil.dip2px(mContext, 15);
            //上下padding
            int top = DensityUtil.dip2px(mContext, 6);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView tv = new TextView(mContext.getApplicationContext());
            tv.setPadding(left, top, left, top);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setText(msg);
            toast.setView(tv);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * toast方法自定义位置
     *
     * @param mContext 上下文
     * @param msg      通知信息
     * @param xLocal   X坐标（dip）
     * @param yLocal   Y坐标（dip）
     */
    public static void toastDiy(Context mContext, String msg, int xLocal, int yLocal) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            //左右padding
            int left = DensityUtil.dip2px(mContext, 15);
            //上下padding
            int top = DensityUtil.dip2px(mContext, 6);
            int x = DensityUtil.dip2px(mContext, xLocal);
            int y = DensityUtil.dip2px(mContext, yLocal);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, x, y);
            TextView tv = new TextView(mContext.getApplicationContext());
            tv.setPadding(left, top, left, top);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setText(msg);
            toast.setView(tv);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * toast方法 显示在底部
     *
     * @param mContext 上下文
     * @param msg      通知信息
     * @param distance 与底部距离（dip）
     */
    public static void toastBottom(Context mContext, String msg, int distance) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            int buttom = DensityUtil.dip2px(mContext, distance);
            int left = DensityUtil.dip2px(mContext, 15);
            int top = DensityUtil.dip2px(mContext, 6);
            toast.setGravity(Gravity.BOTTOM, 0, buttom);
            TextView tv = new TextView(mContext.getApplicationContext());
            tv.setPadding(left, top, left, top);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tv.setText(msg);
            toast.setView(tv);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public static void snackySuccess(Activity activity, String info) {
        Snacky.builder()
                .setActivty(activity)
                .setText("" + info)
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
    }

    public static void snackyWarning(Activity activity, String info) {
        Snacky.builder()
                .setActivty(activity)
                .setText("" + info)
                .setDuration(Snacky.LENGTH_SHORT)
                .warning()
                .show();
    }

    public static void snackyError(Activity activity, String info) {
        Snacky.builder()
                .setActivty(activity)
                .setText("" + info)
                .setDuration(Snacky.LENGTH_SHORT)
                .error()
                .show();
    }

    public static void snackyInfo(Activity activity, String info) {
        Snacky.builder()
                .setActivty(activity)
                .setText("" + info)
                .setDuration(Snacky.LENGTH_SHORT)
                .info()
                .show();
    }

}
