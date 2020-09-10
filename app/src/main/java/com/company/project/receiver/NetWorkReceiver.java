package com.company.project.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.company.project.BuildConfig;
import com.company.project.util.TLog;

import java.util.List;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_DISABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLING;
import static android.net.wifi.WifiManager.WIFI_STATE_UNKNOWN;

/**
 * @author Administrator
 */
public class NetWorkReceiver extends BroadcastReceiver {

    private boolean hasOnWifi = false;

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            TLog.i("NetWorkReceiver", procInfo.processName);
            if (procInfo.pid == pid) {
                TLog.i("NetWorkReceiver-this", procInfo.processName);
                return procInfo.processName;
            }
        }
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!BuildConfig.APPLICATION_ID.equals(getProcessName(context, android.os.Process.myPid()))) {
            TLog.i("NetWorkReceiver", getProcessName(context, android.os.Process.myPid()) + "非本进程");
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            boolean isWifi = networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            if (isWifi && !hasOnWifi) {
                hasOnWifi = true;
            }
            if (!isWifi && hasOnWifi) {
                hasOnWifi = false;
            }
        }
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WIFI_STATE_UNKNOWN)) {
                case WIFI_STATE_DISABLED: {
                    Log.i("NetWorkReceiver", "WiFi关闭");
                    break;
                }
                case WIFI_STATE_DISABLING: {
                    Log.i("NetWorkReceiver", "WiFi正在关闭");
                    break;
                }
                case WIFI_STATE_ENABLED: {
                    Log.i("NetWorkReceiver", "WiFi开启");
                    break;
                }
                case WIFI_STATE_ENABLING: {
                    Log.i("NetWorkReceiver", "WiFi正在开启");
                    break;
                }
                case WIFI_STATE_UNKNOWN: {
                    Log.i("NetWorkReceiver", "WiFi状态未知");
                    break;
                }
            }
        }
    }

}
