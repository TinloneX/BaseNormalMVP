package com.company.project.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.company.project.BuildConfig;
import com.company.project.MyApplication;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * @author zhaojinlong
 * @date 2016/12/20 0020
 */
public class TLog {

    private static String TAG = "tag";
    private static boolean showLog = BuildConfig.DEBUG;

    private TLog() {
    }

    public static void d(Object object) {
        if (showLog) {
            Log.d(TAG, valueOf(object));
        }
    }

    public static void d(String tag, Object object) {
        if (showLog) {
            Log.d(tag, valueOf(object));
        }
    }

    public static void i(Object object) {
        if (showLog) {
            Log.i(TAG, valueOf(object));
        }
    }

    public static void i(String tag, Object object) {
        if (showLog) {
            Log.i(tag, valueOf(object));
        }
    }

    public static void i(String msg, Object... objs) {
        if (showLog) {
            Log.i(TAG, valueOf(msg, objs));
        }
    }

    public static void i(String tag, String msg, Object... objs) {
        if (showLog) {
            Log.i(tag, valueOf(msg, objs));
        }
    }

    public static void e(Object object) {
        if (showLog) {
            Log.e(TAG, valueOf(object));
        }
    }

    public static void e(String msg, Object... objs) {
        if (showLog) {
            Log.e(TAG, valueOf(msg, objs));
        }
    }

    public static void e(String tag, Object object) {
        if (showLog) {
            Log.e(tag, valueOf(object));
        }
    }

    public static void w(Object object) {
        if (showLog) {
            Log.w(TAG, valueOf(object));
        }
    }

    public static void w(String tag, Object object) {
        if (showLog) {
            Log.w(tag, valueOf(object));
        }
    }

    public static void f(Object object) {
        if (showLog) {
            Log.i(TAG, "#- LEVEL_D -#" + valueOf(object));
        }
    }

    public static void f(String tag, Object object) {
        if (showLog) {
            Log.i(tag, "#-----FOCUS-HERE-----#" + valueOf(object));
        }
    }

    public static void th(Throwable object) {
        if (showLog) {
            Log.e(TAG, "这里有异常！！", object);
        }
    }

    public static String valueOf() {
        return "";
    }

    public static String valueOf(Object object) {
        if (object instanceof Throwable) {
            return getStackTraceString((Throwable) object);
        }
        return object == null ? "null" : object.toString();
    }

    public static void keep(Object object) {
        Log.i(TAG, valueOf(object));
    }

    public static void keep(String tag, Object object) {
        i(tag, object);
    }

    public static String valueOf(String msg, Object... objs) {
        try {
            return String.format(msg, objs);
        } catch (Exception e) {
            e.printStackTrace();
            return "---WARNING--- 占位符对应格式有误:" + msg;
        }
    }

    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    @SuppressLint("HardwareIds")
    private static void phoneInfo(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int heapSize = manager != null ? manager.getMemoryClass() : 0;
            keep("系统分配内存大小为：" + heapSize + " M ");
            keep("手机品牌型号：" + Build.BRAND + " - " + Build.MODEL);
            keep("设备厂商：" + Build.BOARD + "  " + Build.MANUFACTURER);
            keep("包名：" + BuildConfig.APPLICATION_ID + "   版本号:" + BuildConfig.VERSION_NAME);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                keep("Umeng_Channel:" + PhoneStateUtil.getChannel(MyApplication.getAppContext()));
                TelephonyManager mTm = (TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE));
                if (mTm != null) {
                    keep(valueOf("IMEI:%s, IMSI: %s, NUMBER:%s, ", mTm.getDeviceId(), mTm.getSubscriberId(), mTm.getLine1Number()));
                }
            } else {
                keep("No Permission : Manifest.permission.READ_PHONE_STATE, 无法获取IMEI");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info(Object object) {
        if (showLog) {
            Log.i("phoneInfo", valueOf(object));
        }
    }
}
