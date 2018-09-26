package com.company.project;


import android.app.Application;

import com.company.project.manager.AppLifecycleManager;

/**
 * @author EDZ
 * @date 2018/3/23.
 * I cannot choose the best. The best chooses me.
 */
public class MyApplication extends Application {

    private static MyApplication mContext;

    public static MyApplication getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppExceptionHandler.getInstance().init(mContext);
        AppLifecycleManager.onAppStart();
    }
}
