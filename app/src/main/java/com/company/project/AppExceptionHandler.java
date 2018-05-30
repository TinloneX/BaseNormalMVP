package com.company.project;

import android.content.Context;
import android.content.Intent;

import com.company.project.activity.LauncherActivity;

/**
 * * **************************************************
 * 文件名称 : FragmentOne
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/28 20:30
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/28 1.00 初始版本
 * **************************************************
 */
public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static AppExceptionHandler instance;
    private Context mContext;

    private AppExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static AppExceptionHandler getInstance() {
        if (instance == null) {
            instance = new AppExceptionHandler();
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent(mContext, LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
