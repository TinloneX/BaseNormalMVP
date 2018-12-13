package com.company.project.manager;

import java.util.PriorityQueue;

/**
 * **************************************************
 * 文件名称 : AppLifecycleManager
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/28 16:33
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/28 1.00 初始版本
 * 修改历史 : 2018/12/12 1.1 简化代码
 * **************************************************
 */
public class AppLifecycleManager {

    private static PriorityQueue<AppLifecycle> singletons = new PriorityQueue<>(5, (a0, a1) -> {
        if (a0 == null || a1 == null)
            return 0;
        return Integer.compare(a1.priority(), a0.priority());
    });

    public static void registerSingleton(AppLifecycle... appLifecycles) {
        if (appLifecycles == null) {
            return;
        }
        AppLifecycle[] ls = appLifecycles;
        for (AppLifecycle l : ls) {
            if (!singletons.contains(l)) {
                singletons.add(l);
            }
        }
    }

    public static void onAppStart() {
        for (AppLifecycle singleton : singletons) {
            singleton.onAppStart();
        }
    }

    public static void onAppEnterBackground() {
        for (AppLifecycle singleton : singletons) {
            singleton.onAppEnterBackground();
        }
    }

    public static void onAppEnterForeground() {
        for (AppLifecycle singleton : singletons) {
            singleton.onAppEnterForeground();
        }
    }

}
