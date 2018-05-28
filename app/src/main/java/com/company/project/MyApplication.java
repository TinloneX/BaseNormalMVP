package com.company.project;

import android.support.multidex.MultiDexApplication;

import com.company.project.manager.AppLifecycleManager;
import com.umeng.commonsdk.UMConfigure;

/**
 * @author EDZ
 * @date 2018/3/23.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppLifecycleManager.onAppStart();
        // 初始化友盟统计
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"Umeng app key");
    }

    public static MyApplication getAppContext() {
        return mContext;
    }
}
