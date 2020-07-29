package com.company.project.adapter.base.divider;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 非原作者本人编写
 * RecyclerView.FlexibleDividerDecoration分割线抽象类
 */
public abstract class BaseFlexibleDividerDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_SIZE = 2;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    protected DividerType mDividerType;
    protected VisibilityProvider mVisibilityProvider;
    protected PaintProvider mPaintProvider;
    protected ColorProvider mColorProvider;
    protected DrawableProvider mDrawableProvider;
    protected SizeProvider mSizeProvider;
    protected boolean mShowLastDivider;
    protected boolean mPositionInsideItem;
    private Paint mPaint;

    protected BaseFlexibleDividerDecoration(@NonNull Builder builder) {
        if (builder.mPaintProvider != null) {
            mDividerType = DividerType.PAINT;
            mPaintProvider = builder.mPaintProvider;
        } else if (builder.mColorProvider != null) {
            mDividerType = DividerType.COLOR;
            mColorProvider = builder.mColorProvider;
            mPaint = new Paint();
            setSizeProvider(builder);
        } else {
            mDividerType = DividerType.DRAWABLE;
            if (builder.mDrawableProvider == null) {
                TypedArray a = builder.mContext.obtainStyledAttributes(ATTRS);
                final Drawable divider = a.getDrawable(0);
                a.recycle();
                mDrawableProvider = (position, parent) -> divider;
            } else {
                mDrawableProvider = builder.mDrawableProvider;
            }
            mSizeProvider = builder.mSizeProvider;
        }
        mVisibilityProvider = builder.mVisibilityProvider;
        mShowLastDivider = builder.mShowLastDivider;
        mPositionInsideItem = builder.mPositionInsideItem;
    }

    private void setSizeProvider(Builder builder) {
        mSizeProvider = builder.mSizeProvider;
        if (mSizeProvider == null) {
            mSizeProvider = (position, parent) -> DEFAULT_SIZE;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int itemCount = adapter.getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        int validChildCount = parent.getChildCount();
        int lastChildPosition = -1;
        for (int i = 0; i < validChildCount; i++) {
            View child = parent.getChildAt(i);
            int childPosition = parent.getChildAdapterPosition(child);
            if (childPosition < lastChildPosition) {
                // Avoid remaining divider when animation starts
                continue;
            }
            lastChildPosition = childPosition;
            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
                // Don't draw divider for last line if mShowLastDivider = false
                continue;
            }
            if (wasDividerAlreadyDrawn(childPosition, parent)) {
                // No need to draw divider again as it was drawn already by previous column
                continue;
            }
            int groupIndex = getGroupIndex(childPosition, parent);
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue;
            }
            Rect bounds = getDividerBound(groupIndex, parent, child);
            switch (mDividerType) {
                case DRAWABLE:
                    Drawable drawable = mDrawableProvider.drawableProvider(groupIndex, parent);
                    drawable.setBounds(bounds);
                    drawable.draw(c);
                    break;
                case PAINT:
                    mPaint = mPaintProvider.dividerPaint(groupIndex, parent);
                    c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
                case COLOR:
                    mPaint.setColor(mColorProvider.dividerColor(groupIndex, parent));
                    mPaint.setStrokeWidth(mSizeProvider.dividerSize(groupIndex, parent));
                    c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect rect, View v, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(v);
        int itemCount = parent.getAdapter().getItemCount();
        int lastDividerOffset = getLastDividerOffset(parent);
        if (!mShowLastDivider && position >= itemCount - lastDividerOffset) {
            // Don't set item offset for last line if mShowLastDivider = false
            return;
        }
        int groupIndex = getGroupIndex(position, parent);
        if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
            return;
        }
        setItemOffsets(rect, groupIndex, parent);
    }

    /**
     * @param parent RecyclerView
     * @return true if recyclerview is reverse layout
     * Check if recyclerview is reverse layout
     */
    protected boolean isReverseLayout(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return layoutManager instanceof LinearLayoutManager && ((LinearLayoutManager) layoutManager).getReverseLayout();
    }

    /**
     * @param parent RecyclerView
     * @return offset for how many views we don't have to draw a divider or 1 if its a
     * In the case mShowLastDivider = false, Returns offset for how many views we don't have to draw a divider for, for LinearLayoutManager it is as simple as not drawing the last child divider, but for a GridLayoutManager it needs to take the span count for the last items into account until we use the span count configured for the grid.
     */
    private int getLastDividerOffset(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            for (int i = itemCount - 1; i >= 0; i--) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i;
                }
            }
        }
        return 1;
    }

    /**
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return true if the divider can be skipped as it is in the same row as the previous one.
     * Determines whether divider was already drawn for the row the item is in,effectively only makes sense for a grid
     */
    private boolean wasDividerAlreadyDrawn(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanIndex(position, spanCount) > 0;
        }
        return false;
    }

    /**
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return group index of items
     * Returns a group index for GridLayoutManager. for LinearLayoutManager, always returns position.
     */
    private int getGroupIndex(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanGroupIndex(position, spanCount);
        }
        return position;
    }

    protected abstract Rect getDividerBound(int position, RecyclerView parent, View child);

    protected abstract void setItemOffsets(Rect outRect, int position, RecyclerView parent);

    protected enum DividerType {
        DRAWABLE, PAINT, COLOR
    }

    /**
     * Interface for controlling divider visibility
     */
    public interface VisibilityProvider {
        /**
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return True if the divider at position should be hidden
         * Returns true if divider should be hidden.
         */
        boolean shouldHideDivider(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling paint instance for divider drawing
     */
    public interface PaintProvider {
        /**
         * Returns {@link Paint} for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Paint instance
         */
        Paint dividerPaint(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling divider color
     */
    public interface ColorProvider {
        /**
         * Returns {@link android.graphics.Color} value of divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Color value
         */
        int dividerColor(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling drawable object for divider drawing
     */
    public interface DrawableProvider {
        /**
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Drawable instance
         * Returns drawable instance for divider
         */
        Drawable drawableProvider(int position, RecyclerView parent);
    }

    /**
     * Interface for controlling divider size
     */
    public interface SizeProvider {
        /**
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Size of divider
         * Returns size value of divider.Height for horizontal divider, width for vertical divider
         */
        int dividerSize(int position, RecyclerView parent);
    }

    public static class Builder<T extends Builder> {
        protected Resources mResources;
        private Context mContext;
        private PaintProvider mPaintProvider;
        private ColorProvider mColorProvider;
        private DrawableProvider mDrawableProvider;
        private SizeProvider mSizeProvider;
        private VisibilityProvider mVisibilityProvider = (position, parent) -> false;
        private boolean mShowLastDivider = false;
        private boolean mPositionInsideItem = false;

        public Builder(Context context) {
            mContext = context;
            mResources = context.getResources();
        }

        public T paint(final Paint paint) {
            return paintProvider((position, parent) -> paint);
        }

        public T paintProvider(PaintProvider provider) {
            mPaintProvider = provider;
            return (T) this;
        }

        public T color(final int color) {
            return colorProvider((position, parent) -> color);
        }

        public T colorResId(@ColorRes int colorId) {
            return color(ContextCompat.getColor(mContext, colorId));
        }

        public T colorProvider(ColorProvider provider) {
            mColorProvider = provider;
            return (T) this;
        }

        public T drawable(@DrawableRes int id) {
            return drawable(ContextCompat.getDrawable(mContext, id));
        }

        public T drawable(final Drawable drawable) {
            return drawableProvider((position, parent) -> drawable);
        }

        public T drawableProvider(DrawableProvider provider) {
            mDrawableProvider = provider;
            return (T) this;
        }

        public T size(final int size) {
            return sizeProvider((position, parent) -> size);
        }

        public T sizeResId(@DimenRes int sizeId) {
            return size(mResources.getDimensionPixelSize(sizeId));
        }

        public T sizeProvider(SizeProvider provider) {
            mSizeProvider = provider;
            return (T) this;
        }

        public T visibilityProvider(VisibilityProvider provider) {
            mVisibilityProvider = provider;
            return (T) this;
        }

        public T showLastDivider() {
            mShowLastDivider = true;
            return (T) this;
        }

        public T positionInsideItem(boolean positionInsideItem) {
            mPositionInsideItem = positionInsideItem;
            return (T) this;
        }

        protected void checkBuilderParams() {
            if (mPaintProvider != null) {
                if (mColorProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setColor method of Paint class to specify line color. Do not provider ColorProvider if you set PaintProvider.");
                }
                if (mSizeProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setStrokeWidth method of Paint class to specify line size. Do not provider SizeProvider if you set PaintProvider.");
                }
            }
        }
    }
}