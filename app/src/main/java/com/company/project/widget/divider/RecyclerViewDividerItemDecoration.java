package com.company.project.widget.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView简单分割线
 */
public class RecyclerViewDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight = 2;
    private Paint mPaint;

    public RecyclerViewDividerItemDecoration() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#00ffffff"));
    }

    public RecyclerViewDividerItemDecoration(String colorStr) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(colorStr));
    }

    public RecyclerViewDividerItemDecoration(int color) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    public RecyclerViewDividerItemDecoration(String colorStr, int dividerHeight) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(colorStr));
        this.dividerHeight = dividerHeight;
    }

    public RecyclerViewDividerItemDecoration(int color, int dividerHeight) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //如果不是第一个则设置top的值
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码1px
            outRect.top = dividerHeight;
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(childView);
            //第一个item不需要绘制
            if (index == 0) {
                continue;
            }

            //坐标点
            int dividerTop = childView.getTop() - dividerHeight;
            int dividerButtom = childView.getTop();
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerButtom, mPaint);
        }

    }
}
