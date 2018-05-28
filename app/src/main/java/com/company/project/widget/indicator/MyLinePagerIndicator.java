package com.company.project.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;

public class MyLinePagerIndicator extends LinePagerIndicatorEx {

    public MyLinePagerIndicator(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LinearGradient lg = new LinearGradient(getLineRect().left, getLineRect().top, getLineRect().right, getLineRect().bottom, new int[]{0xFFD3B151, 0xFFF6DD99}, null, LinearGradient.TileMode.CLAMP);
        getPaint().setShader(lg);
        canvas.drawRoundRect(getLineRect(), getRoundRadius(), getRoundRadius(), getPaint());
    }
}
