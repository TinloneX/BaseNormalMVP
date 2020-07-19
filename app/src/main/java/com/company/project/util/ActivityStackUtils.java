package com.company.project.util;

import android.app.Activity;

import androidx.collection.ArrayMap;

import java.util.Map;
import java.util.Stack;

/**
 * 手动管理活动栈结构
 * 慎用，容易产生内存泄漏
 * @author Administrator
 */
public class ActivityStackUtils {

    private static ArrayMap<String, Stack<Activity>> mStackPool = new ArrayMap<>();

    public static Stack<Activity> requestStack(String tag) {
        if (mStackPool.containsKey(tag)) {
            return mStackPool.get(tag);
        } else {
            Stack<Activity> stack = new Stack<>();
            mStackPool.put(tag, stack);
            return stack;
        }
    }

    public static Activity peekTopActivity(String tag) {
        return requestStack(tag).peek();
    }

    /**
     * 压入Activity
     *
     * @param tag      栈标志信息
     * @param activity 入栈界面
     */
    public static void pressActivity(String tag, Activity activity) {
        requestStack(tag).push(activity);
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
        Stack<Activity> activities = requestStack(tag);
        while (!activities.empty()) {
            activities.pop().finish();
        }
        //此处不释放，将空集合置于池中，以待后续使用
        activities.clear();
    }

    /**
     * 释放栈池资源
     */
    public void release() {
        for (Map.Entry<String, Stack<Activity>> entry : mStackPool.entrySet()) {
            finishAll(entry.getKey());
        }
        mStackPool.clear();
    }
}
