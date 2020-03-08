package com.company.project.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

/**
 * 网络管理工具
 *
 * @author Tinlone
 */
public class NetWorkUtils {

    /**
     * 检查网络是否可用
     *
     * @param paramContext paramContext
     * @return return
     */
    public static boolean isNetConnected(@NonNull Context paramContext) {
        ConnectivityManager systemService = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo localNetworkInfo = null;
        if (systemService != null) {
            localNetworkInfo = systemService.getActiveNetworkInfo();
        }
        return (localNetworkInfo != null) && (localNetworkInfo.isAvailable());
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 检测3G是否连接
     */
    public static boolean is4gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }
}
