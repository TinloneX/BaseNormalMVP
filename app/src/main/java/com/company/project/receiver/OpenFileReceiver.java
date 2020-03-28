package com.company.project.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.company.project.util.FileUtils;
import com.company.project.util.TLog;

/**
 * @author Administrator
 * 根据文件类型自动获取打开文件的intent
 */
public class OpenFileReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.company.project.ACTION_OPEN_FILE";
    public static final String KEY_PATH = "path";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (ACTION.equals(intent.getAction())) {
                String path = intent.getStringExtra(KEY_PATH);
                TLog.i(path);
                if (PackageManager.PERMISSION_GRANTED ==
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent dealIntent = FileUtils.getFileOpenIntent(path);
                    context.startActivity(dealIntent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
