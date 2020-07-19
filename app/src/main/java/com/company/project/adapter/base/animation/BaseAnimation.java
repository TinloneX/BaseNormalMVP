package com.company.project.adapter.base.animation;

import android.animation.Animator;
import android.view.View;

/**
 * @author ynet
 *  基础动画抽象类
 */
public abstract class BaseAnimation {
    /**
     *  获取Animator[]
     */
    public abstract Animator[] getAnimators(View view);
}