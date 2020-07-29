package com.company.project.adapter.base;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.chad.library.adapter.base.animation.SlideInLeftAnimation;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author 原作者为chenjinping
 * @version 0.0.2
 * 修改CommonRecyclerViewAdapter
 * 增加头部，底部和动画
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    public static final int ALPHAIN = 0x00000001;
    public static final int SCALEIN = 0x00000002;
    public static final int SLIDEIN_LEFT = 0x00000004;
    public static final int SLIDEIN_RIGHT = 0x00000005;
    public static final int HEADER_VIEW = 0x00011111;
    public static final int FOOTER_VIEW = 0x00033333;
    public static final int EMPTY_VIEW = 0x00055555;
    protected List<T> mList;
    private RecyclerItemClickListener itemClickListener;
    private RecyclerItemLongClickListener itemLongClickListener;
    private int mDuration = 800;
    private int mNotDoAnimationCount = 0;
    private boolean mOpenAnimationEnable = false;
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    //header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    //empty
    private FrameLayout mEmptyLayout;
    private boolean mHeadAndEmptyEnable = true;
    private boolean mFootAndEmptyEnable = true;

    public BaseRecyclerViewAdapter() {
        mList = new ArrayList<>();
    }

    public abstract void onBindViewHolderEvent(final CommonViewHolder holder, final int position);

    public abstract CommonViewHolder onCreateViewHolderEvent(ViewGroup parent, int position);

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        switch (position) {
            case HEADER_VIEW:
                return CommonViewHolder.createViewHolder(parent.getContext(), mHeaderLayout);
            case EMPTY_VIEW:
                return CommonViewHolder.createViewHolder(parent.getContext(), mEmptyLayout);
            case FOOTER_VIEW:
                return CommonViewHolder.createViewHolder(parent.getContext(), mFooterLayout);
            default:
                break;
        }
        return onCreateViewHolderEvent(parent, position);
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, final int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HEADER_VIEW:
            case EMPTY_VIEW:
            case FOOTER_VIEW:
                break;
            case 0:
            default:
                if (getItem(position - getHeaderLayoutCount()) == null) {
                    return;
                }
                onBindViewHolderEvent(holder, (position - getHeaderLayoutCount()) >= mList.size()
                        ? mList.size() - 1 : (position - getHeaderLayoutCount()));
                if (null != this.itemClickListener) {
                    holder.getConvertView().setOnClickListener(v ->
                            itemClickListener.onItemClick(holder.getConvertView(),
                                    position - getHeaderLayoutCount()));
                }
                if (null != this.itemLongClickListener) {
                    holder.getConvertView().setOnLongClickListener(v -> {
                        itemLongClickListener.onItemLongClick(holder.getConvertView(),
                                position - getHeaderLayoutCount());
                        return false;
                    });
                }
                break;
        }
    }

    public void setDataList(List<T> recyclerList) {
        setDataList(recyclerList, true);
    }

    public void setDataList(List<T> recyclerList, boolean clearable) {
        if (null == recyclerList) {
            return;
        }
        if (null != this.mList) {
            if (clearable) {
                this.mList.clear();
            }
            this.mList.addAll(recyclerList);
            this.notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        return (position >= 0 && position < mList.size()) ? mList.get(position) : null;
    }

    public List<T> getData() {
        return mList;
    }

    @Override
    public int getItemCount() {
        int count;
        if (getEmptyViewCount() == 1) {
            count = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++;
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++;
            }
        } else {
            count = getHeaderLayoutCount() + mList.size() + getFooterLayoutCount();
        }
        return count;
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public RecyclerItemClickListener getItemClickListener() {
        return this.itemClickListener;
    }

    public void setOnItemLongClickListener(RecyclerItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public boolean isOutOfIndex(int position) {
        return mList == null || position < 0 || position >= mList.size();
    }

    @Override
    public void onViewAttachedToWindow(CommonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (mOpenAnimationEnable) {
            if (holder.getLayoutPosition() >= mNotDoAnimationCount) {
                BaseAnimation animation;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    anim.setDuration(mDuration).start();
                }
            }
        }
    }

    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    public void closeLoadAnimation() {
        this.mOpenAnimationEnable = false;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setNotDoAnimationCount(int notDoAnimationCount) {
        this.mNotDoAnimationCount = notDoAnimationCount;
    }

    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return mHeaderLayout.getChildCount();
    }

    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return mFooterLayout.getChildCount();
    }

    public int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (mList.size() != 0) {
            return 0;
        }
        return 1;
    }

    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * Return root layout of footer
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mList.size();
            if (adjPosition < adapterCount) {
                if (hasViewType()) {
                    return multipleView(adjPosition);
                } else {
                    return super.getItemViewType(position);
                }
            } else {
                return FOOTER_VIEW;
            }
        }
    }

    protected boolean hasViewType() {
        return false;
    }

    public int multipleView(int position) {
        return position;
    }

    public int addHeaderView(View header) {
        return addHeaderView(header, -1);
    }

    public int addHeaderView(View header, int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    private int getHeaderViewPosition() {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            if (mHeadAndEmptyEnable) {
                return 0;
            }
        } else {
            return 0;
        }
        return -1;
    }

    private int getFooterViewPosition() {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            int position = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++;
            }
            if (mFootAndEmptyEnable) {
                return position;
            }
        } else {
            return getHeaderLayoutCount() + mList.size();
        }
        return -1;
    }

    public int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    public void removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0) {
            return;
        }
        mHeaderLayout.removeAllViews();
        int position = getHeaderViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (getFooterLayoutCount() == 0) {
            return;
        }
        mFooterLayout.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }
}
