package com.company.project.util;

import android.app.Activity;

import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * 手动管理活动栈结构
 * 慎用，容易产生内存泄漏
 */
public class ActivityStackUtils {

    private static ArrayMap<String, ArrayList<Activity>> mStackPool = new ArrayMap<>();

    public static ArrayList<Activity> requestStack(String tag) {
        if (mStackPool.containsKey(tag)) {
            return mStackPool.get(tag);
        } else {
            ArrayList<Activity> stack = new ArrayList<>();
            mStackPool.put(tag, stack);
            return stack;
        }
    }

    /**
     * 压入Activity
     *
     * @param tag      栈标志信息
     * @param activity 入栈界面
     */
    public static void pressActivity(String tag, Activity activity) {
        requestStack(tag).add(activity);
    }

    /**
     * 弹出Activity
     *
     * @param tag      栈标志信息
     * @param activity 入栈界面
     */
    public static void popActivity(String tag, Activity activity) {
        requestStack(tag).remove(activity);
    }

    /**
     * 结束指定栈所有Activity
     *
     * @param tag 栈标志信息
     */
    public static void finishAll(String tag) {
        ArrayList<Activity> activities = requestStack(tag);
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
            iterator.remove();
        }
        //此处不释放，将空集合置于池中，以待后续使用
        activities.clear();
    }

    /**
     * 释放栈池资源
     */
    public void release() {
        for (Map.Entry<String, ArrayList<Activity>> entry : mStackPool.entrySet()) {
            entry.getValue().clear();
        }
        mStackPool.clear();
    }
}
