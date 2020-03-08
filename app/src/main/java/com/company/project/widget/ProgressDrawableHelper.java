package com.company.project.widget;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Size;

import com.company.project.util.DensityUtil;

/**
 * 动态改变ProgressBar的progressDrawable属性
 */
public class ProgressDrawableHelper {

    public static void change(ProgressBar progressBar, @Size(min = 1) String colorStr) {
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        change(progressBar, Color.parseColor(colorStr), layoutParams.height / 2);
    }

    public static void change(ProgressBar progressBar, @ColorInt int color) {
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        change(progressBar, color, layoutParams.height / 2);
    }

    public static void change(ProgressBar progressBar, @ColorInt int color, @IntRange(from = 0) int radius) {
        //获取progressBar的LayerDrawable,LayerDrawable是我们写layer-list生成的多层级的drawable
        LayerDrawable drawable = (LayerDrawable) progressBar.getProgressDrawable();
        //新建一个GradientDrawable ，shape标签生成的drawable类型，此处用它是为了设置圆角，若不需要圆角，可以用ColorDrawable设置颜色
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(DensityUtil.dip2px(progressBar.getContext(), radius));
        gradientDrawable.setColor(color);
        //ClipDrawable 是一个ClipDrawable是通过设置一个Drawable的当前显示比例来裁剪出另一张Drawable，
        //你可以通过调节这个比例来控制裁剪的宽高，以及裁剪内容占整个容器的权重，不是ClipDrawable的话会无法根据我们的progress显示长度，进度条显示起来总是满进度的
        ClipDrawable clipDrawable = new ClipDrawable(gradientDrawable, Gravity.START, ClipDrawable.HORIZONTAL);
        //重点，将LayerDrawable 里id为android.R.id.progress的Drawable层替换为我们新建的Drawable，这就达到了我们改变他的颜色的目的了
        drawable.setDrawableByLayerId(android.R.id.progress, clipDrawable);
        progressBar.setProgressDrawable(drawable);
    }

    public static LayerDrawable generator(ProgressBar progressBar, @Size(min = 1) String colorStr) {
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        return generator(progressBar, Color.parseColor(colorStr), layoutParams.height / 2);
    }

    public static LayerDrawable generator(ProgressBar progressBar, @ColorInt int color) {
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        return generator(progressBar, color, layoutParams.height / 2);
    }

    public static LayerDrawable generator(ProgressBar progressBar, @ColorInt int color, @IntRange(from = 0) int radius) {
        //获取progressBar的LayerDrawable,LayerDrawable是我们写layer-list生成的多层级的drawable
        LayerDrawable drawable = (LayerDrawable) progressBar.getProgressDrawable();
        //新建一个GradientDrawable ，shape标签生成的drawable类型，此处用它是为了设置圆角，若不需要圆角，可以用ColorDrawable设置颜色
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(DensityUtil.dip2px(progressBar.getContext(), radius));
        gradientDrawable.setColor(color);
        //ClipDrawable 是一个ClipDrawable是通过设置一个Drawable的当前显示比例来裁剪出另一张Drawable，
        //你可以通过调节这个比例来控制裁剪的宽高，以及裁剪内容占整个容器的权重，不是ClipDrawable的话会无法根据我们的progress显示长度，进度条显示起来总是满进度的
        ClipDrawable clipDrawable = new ClipDrawable(gradientDrawable, Gravity.START, ClipDrawable.HORIZONTAL);
        //重点，将LayerDrawable 里id为android.R.id.progress的Drawable层替换为我们新建的Drawable，这就达到了我们改变他的颜色的目的了
        drawable.setDrawableByLayerId(android.R.id.progress, clipDrawable);
        return drawable;
    }

}
