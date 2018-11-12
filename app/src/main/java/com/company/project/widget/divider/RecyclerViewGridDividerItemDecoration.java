package com.company.project.widget.divider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * @author Administrator
 * for grid 井形
 */

public class RecyclerViewGridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight = 1;
    private int dividerWidth = 1;
    //    int
    private Paint mPaint;

    public RecyclerViewGridDividerItemDecoration() {
        super();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#898989"));
    }

    public RecyclerViewGridDividerItemDecoration(String colorStr) {
        super();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        try {
            mPaint.setColor(Color.parseColor(colorStr));
        } catch (Exception e) {
            mPaint.setColor(Color.parseColor("#898989"));
        }
    }

    public RecyclerViewGridDividerItemDecoration(String colorStr, int dividerWidth, int dividerHeight) {
        super();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        try {
            mPaint.setColor(Color.parseColor(colorStr));
        } catch (Exception e) {
            mPaint.setColor(Color.parseColor("#898989"));
        }
        this.dividerWidth = dividerWidth;
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager gm = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = gm.getSpanCount();
        int position = parent.getChildLayoutPosition(view);
        int column = position % spanCount; // item column
        outRect.left = column * dividerWidth / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = dividerWidth - (column + 1) * dividerWidth / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
            outRect.top = dividerWidth; // item top
        }


    }
}
