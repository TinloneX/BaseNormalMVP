package com.company.project.manager;

/**
 * **************************************************
 * 文件名称 : AppLifecycle
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/28 16:34
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/28 1.00 初始版本
 * **************************************************
 */
public interface AppLifecycle {
    void onAppStart();

    void onAppEnterBackground();

    void onAppEnterForeground();

    int priority();
}
