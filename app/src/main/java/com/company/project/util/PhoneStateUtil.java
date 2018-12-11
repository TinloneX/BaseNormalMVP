package com.company.project.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 手机状态工具类
 */
public class PhoneStateUtil {
    private PhoneStateUtil() {
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
     * 获取手机是否root信息
     */
    public static boolean isRoot() {
        try {
            return (new File("/system/bin/su").exists()) || (new File("/system/xbin/su").exists());
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory() {
        // 获取android当前可用内存大小
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        String fileSize = FileSizeUtil.formatFileSize(blockSize * availableBlocks);
        return "当前可用内存：" + fileSize;
    }


    public static String getChannel(Context context) {
        try {
            String packageName = context.getPackageName();
            ApplicationInfo appInfo;
            appInfo = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        final String[] deviceId = {""};
        PermissionUtils.permission(PermissionConstants.PHONE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    @SuppressLint("HardwareIds")
                    public void onGranted() {
                        TelephonyManager mTm = (TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE));
                        if (mTm != null) {
                            deviceId[0] = mTm.getDeviceId();
                        }
                    }

                    @Override
                    public void onDenied() {
                        TLog.i("No Permission : Manifest.permission.READ_PHONE_STATE, 无法获取IMEI");
                    }
                }).request();
        return deviceId[0];
    }

    /**
     * 获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
        }
        return "手机macAdd:" + result;
    }

    /***
     * 获取 IMEI
     *
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Context context) {
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (telephonyManager != null) {
                imei = String.valueOf(telephonyManager.getDeviceId());
            }
        }
        return imei;
    }

    /**
     * SIM卡运营商。如中国移动，中国联通，中国电信
     */
    @SuppressLint({"HardwareIds"})
    public static String getOperators(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = null;
        String IMSI = null;
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                IMSI = tm.getSubscriberId();
            }
        }
        if (IMSI == null || IMSI.equals("")) {
            return null;
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
     * 手机CPU信息
     */
    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2;
        //1-cpu型号 //2-cpu频率
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException ignored) {
        }
        TLog.i("CPU型号:" + cpuInfo[0] + ", CPU频率：" + cpuInfo[1]);
        return cpuInfo;
    }
}
