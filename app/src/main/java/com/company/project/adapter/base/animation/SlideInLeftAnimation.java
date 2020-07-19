package com.company.project.adapter.base.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author ynet
 *  左部侧滑进入动画
 */
public class SlideInLeftAnimation extends BaseAnimation {
    /**
     *  获取Animator[]
     */
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
        };
    }
}
