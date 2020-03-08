package com.company.project.widget;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.Size;

/**
 * 动态生成{#GradientDrawable}
 * shape
 */
public class GradientDrawableGenerator {

    private GradientDrawableGenerator() {
    }

    public static GradientDrawable generate(int strokeWidth, int radius, @Size(min = 1) String strokeColor, @Size(min = 1) String backgroundColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setStroke(strokeWidth, Color.parseColor(strokeColor));
        drawable.setColor(Color.parseColor(backgroundColor));
        return drawable;
    }

    public static GradientDrawable generate(int radius, @Size(min = 1) String strokeColor, @Size(min = 1) String backgroundColor) {
        return generate(1, radius, strokeColor, backgroundColor);
    }

    public static GradientDrawable generate(@Size(min = 1) String strokeColor, @Size(min = 1) String backgroundColor) {
        return generate(1, 0, strokeColor, backgroundColor);
    }

    public static GradientDrawable generate(@Size(min = 1) String strokeColor) {
        return generate(1, 0, strokeColor, "#FFFFFF");
    }


}
