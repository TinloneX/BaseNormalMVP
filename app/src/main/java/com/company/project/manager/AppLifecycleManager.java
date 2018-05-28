package com.company.project.manager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * **************************************************
 * 文件名称 : AppLifecycleManager
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/28 16:33
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/28 1.00 初始版本
 * **************************************************
 */
public class AppLifecycleManager {

    private static PriorityQueue<AppLifecycle> singletons = new PriorityQueue<>(5, new Comparator<AppLifecycle>() {
        @Override
        public int compare(AppLifecycle a0, AppLifecycle a1) {
            if (a0 == null || a1 == null)
                return 0;
            if (a0.priority() > a1.priority()) {
                return -1;
            } else if (a0.priority() == a1.priority()) {
                return 0;
            } else {
                return 1;
            }
        }
    });

    public static void registerSingleton(AppLifecycle... appLifecycles) {

        if (appLifecycles == null) {
            return;
        }
        List<AppLifecycle> ls = Arrays.asList(appLifecycles);
        for (AppLifecycle l : ls) {
            if (!singletons.contains(l)) {
                singletons.add(l);
            }
        }
    }

    public static void onAppStart() {
        Iterator<AppLifecycle> iter = singletons.iterator();
        while (iter.hasNext()) {
            iter.next().onAppStart();
        }
    }

    public static void onAppEnterBackground() {
        Iterator<AppLifecycle> iter = singletons.iterator();
        while (iter.hasNext()) {
            iter.next().onAppEnterBackground();
        }
    }

    public static void onAppEnterForeground() {
        Iterator<AppLifecycle> iter = singletons.iterator();
        while (iter.hasNext()) {
            iter.next().onAppEnterForeground();
        }
    }

}
