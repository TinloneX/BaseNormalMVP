package com.company.project.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.TextView;

/**
 * **************************************************
 * 文件名称 : ViewUtils
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/26 10:58
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/26 1.00 初始版本
 * 修改历史 : 2018/12/11 1.1 移除过时方法，拆解不同类工具方法
 * **************************************************
 */
@SuppressWarnings("unused")
public class ViewUtils {

    private ViewUtils() {
    }

    /**
     * 扩大一个View的点击事件接收区域。
     * 默认是在原View四边增加30pix
     */
    public static void expandTouchArea(View view) {
        expandTouchArea(view, 30);
    }

    public static void expandTouchArea(View view, int size) {
        View parentView = (View) view.getParent();
        parentView.post(() -> {
            Rect rect = new Rect();
            view.getHitRect(rect);
            rect.top -= size;
            rect.bottom += size;
            rect.left -= size;
            rect.right += size;
            parentView.setTouchDelegate(new TouchDelegate(rect, view));
        });
    }

    /**
     * 获取设备状态栏的高度（pix）
     */
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * @param type TypedValue.COMPLEX_UNIT_PX
     */
    public static float getAutoFitTextSize(Context context, TextView mTextView, int type, int limit, int precision) {
        if (type == TypedValue.COMPLEX_UNIT_PX) {
            float textSize = mTextView.getTextSize();
            while (true) {
                //计算所有文本占有的屏幕宽度(pix)
                float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString());
                //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
                if (textWidth > limit) {
                    textSize = (int) mTextView.getTextSize();
                    textSize = textSize - precision;
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                } else {
                    break;
                }
            }
            return textSize;
        } else {
            return getAutoFitTextSize(context, mTextView, limit, precision);
        }
    }

    /**
     * 一个TextView限定宽度，然后寻找合适的自适应字体大小textSize
     *
     * @param dipLimitTextViewWidth 必须是dp单位值
     * @return 该字体自适应后的pix字体大小。
     */
    public static float getAutoFitTextSize(Context context, TextView mTextView, int dipLimitTextViewWidth, int precisionAccuracy) {
        float textSize = mTextView.getTextSize();
        //获取dipLimitTextViewWidth转换后的设备pix值。
        int mTextViewWidth = DensityUtil.dip2px(context, dipLimitTextViewWidth);
        while (true) {
            //计算所有文本占有的屏幕宽度(pix)
            float textWidth = mTextView.getPaint().measureText(mTextView.getText().toString());
            //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
            if (textWidth > mTextViewWidth) {
                textSize = (int) mTextView.getTextSize();
                textSize = textSize - precisionAccuracy;
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                break;
            }
        }
        return textSize;
    }

    /**
     * 获取导航栏高度
     *
     * @param context 上下文
     * @return int 高度
     */
    public static int getNavigationHeight(Context context) {
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

}
