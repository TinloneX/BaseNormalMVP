package com.company.project.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;


public class PhoneStateUtil {
    private PhoneStateUtil() {
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

}
