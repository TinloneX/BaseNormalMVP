package com.company.project.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * **************************************************
 * 文件名称 : AppUtil
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/26 10:58
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/26 1.00 初始版本
 * **************************************************
 */
public class AppUtil {


    public static final String SAVE_SEARCH_STOCK_HISTORY = "search_stock_history";
    public static final String ACCOUNT_REGEX = "(\\d{3,4})\\d{4}(\\d{4})";
    public static final String ACCOUNT_REPLACE = "$1****$2";

    /**
     * 获取版本号。
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi.versionCode;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi.versionName;
    }

    /**
     * SIM卡运营商。如中国移动，中国联通，中国电信
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getOperators(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = null;
        String IMSI = tm.getSubscriberId();
        if (IMSI == null || IMSI.equals("")) {
            return operator;
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            operator = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            operator = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            operator = "中国电信";
        }
        return operator;
    }

    /**
     * 网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

    /***
     * 获取 IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = String.valueOf(telephonyManager.getDeviceId());
        return imei;
    }

    /**
     * 获取设备型号，如谷歌nexus
     *
     * @return 设备型号
     */
    public static String getBuildBrandModel() {
        return Build.MODEL;
    }

    /**
     * 扩大一个View的点击事件接收区域。
     * 默认是在原View四边增加30pix
     *
     * @param view
     */
    public static void expandTouchArea(View view) {
        expandTouchArea(view, 30);
    }

    public static void expandTouchArea(View view, int size) {
        View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                view.getHitRect(rect);

                rect.top -= size;
                rect.bottom += size;
                rect.left -= size;
                rect.right += size;

                parentView.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    /**
     * 获取设备状态栏的高度（pix）
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 获得设备的宽和高尺寸(pix)。
     * <p>
     * float[0]存放设备屏幕的宽（pix值），float[1]存放设备屏幕的高（pix值）。
     *
     * @param context
     * @return float[2]
     */
    public static float[] getDeviceDisplaySize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        float[] size = new float[2];
        size[0] = width;
        size[1] = height;

        return size;
    }

    //检测网络是否可用。
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param context
     * @param mTextView
     * @param type      TypedValue.COMPLEX_UNIT_PX
     * @param limit
     * @param precision
     * @return
     */
    public static float getAutofitTextSize(Context context, TextView mTextView, int type, int limit, int precision) {
        if (type == TypedValue.COMPLEX_UNIT_PX) {
            float textSize = mTextView.getTextSize();

            int mTextViewWidth = limit;

            while (true) {
                //计算所有文本占有的屏幕宽度(pix)
                float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString());

                //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
                if (textWidth > mTextViewWidth) {
                    textSize = (int) mTextView.getTextSize();
                    textSize = textSize - precision;
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                } else {
                    break;
                }
            }

            return textSize;
        } else {
            return getAutofitTextSize(context, mTextView, limit, precision);
        }
    }

    /**
     * 一个TextView限定宽度，然后寻找合适的自适应字体大小textSize
     *
     * @param context
     * @param mTextView
     * @param dipLimitTextViewWidth 必须是dp单位值
     * @return 该字体自适应后的pix字体大小。
     */
    public static float getAutofitTextSize(Context context, TextView mTextView, int dipLimitTextViewWidth, int precisionAccuracy) {
        float textSize = mTextView.getTextSize();

        //获取dipLimitTextViewWidth转换后的设备pix值。
        int mTextViewWidth = dip2px(context, dipLimitTextViewWidth);

        while (true) {
            //计算所有文本占有的屏幕宽度(pix)
            float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString());

            //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
            if (textWidth > mTextViewWidth) {
                textSize = (int) mTextView.getTextSize();
                textSize = textSize - precisionAccuracy;
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                break;
            }
        }

        return textSize;
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void toast(Context context, int msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 账号隐藏,显示*
     */
    public static String getHideAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return account;
        }
        return account.replaceAll(ACCOUNT_REGEX, ACCOUNT_REPLACE);

    }

    /**
     * 格式化资产
     *
     * @param value
     * @return
     */
    public static String formatAssets(String value) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        String str;
        float fv = 0f;
        try {
            fv = Float.parseFloat(value);
        } catch (Exception e) {

        }
        if (fv >= 100000.0f || fv <= -100000.0f) {
            str = format.format(fv / 10000.0f) + "万";
        } else {
            str = format.format(fv);
        }
        return str;
    }

    public static String formatAssets2(String value) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        String str;
        float fv = 0f;
        try {
            fv = Float.parseFloat(value);
        } catch (Exception e) {

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
     * toast方法  显示在屏幕顶部
     *
     * @param mContext 上下文
     * @param msg      通知信息
     * @param distance 与顶部距离（dip）
     */
    public static void toastTop(Context mContext, String msg, int distance) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            int topDistance = dip2px(mContext, distance);
            int left = dip2px(mContext, 15);//左右padding
            int top = dip2px(mContext, 6);//上下padding
            toast.setGravity(Gravity.TOP, 0, topDistance);
            TextView tv = new TextView(mContext.getApplicationContext());
//            tv.setBackgroundResource(R.drawable.toast_orange_gradient_bg);
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
            int left = dip2px(mContext, 15);//左右padding
            int top = dip2px(mContext, 6);//上下padding
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView tv = new TextView(mContext.getApplicationContext());
//            tv.setBackgroundResource(R.drawable.toast_orange_gradient_bg);
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
            int left = dip2px(mContext, 15);//左右padding
            int top = dip2px(mContext, 6);//上下padding
            int x = dip2px(mContext, xLocal);
            int y = dip2px(mContext, yLocal);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, x, y);
            TextView tv = new TextView(mContext.getApplicationContext());
//            tv.setBackgroundResource(R.drawable.toast_orange_gradient_bg);
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
    public static void toastButtom(Context mContext, String msg, int distance) {
        if (mContext != null) {
            Toast toast = new Toast(mContext.getApplicationContext());
            int buttom = dip2px(mContext, distance);
            int left = dip2px(mContext, 15);//左右padding
            int top = dip2px(mContext, 6);//上下padding
            toast.setGravity(Gravity.BOTTOM, 0, buttom);
            TextView tv = new TextView(mContext.getApplicationContext());
//            tv.setBackgroundResource(R.drawable.toast_orange_gradient_bg);
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
     * 获取导航栏高度
     *
     * @param context 上下文
     * @return int 高度
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    //    隐藏手机号中间四位
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

    //金额格式化
    public static String formatMoney(String str) {
        if (str.equals("0") || str.equals("0.00")) {
            return str;
        }
        Double cny = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        String CNY = df.format(cny);
        Double cnyd = Double.parseDouble(CNY);
        DecimalFormat df2 = new DecimalFormat("#,###.00");
        String m = df2.format(cnyd);
        System.out.print(m);
        return m;
    }

}
