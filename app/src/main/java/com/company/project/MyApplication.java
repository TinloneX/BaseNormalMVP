package com.company.project;


import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;

import com.company.project.activity.LauncherActivity;
import com.company.project.receiver.OpenFileReceiver;

/**
 * @author Tinlone
 * @date 2018/3/23.
 * I cannot choose the best. The best chooses me.
 */
public class MyApplication extends Application {

    private static class MicroApplication {
        public static MyApplication application;
    }

    @Nullable
    public static MyApplication getAppContext() {
        return MicroApplication.application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MicroApplication.application = this;
        dealUncaughtException();
        registerReceiver(new OpenFileReceiver(), new IntentFilter(BuildConfig.APPLICATION_ID + ".open_file"));
    }

    private void dealUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Intent intent = new Intent(MyApplication.this, LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }
}
