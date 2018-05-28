package com.company.project.widget.indicator;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.company.project.R;
import com.company.project.util.AppUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MySimpleNavigatorAdapter extends CommonNavigatorAdapter {

    private List<String> mTitles;

    private MyLinePagerIndicator mHXLinePagerIndicator;

    private int typeValue = TypedValue.COMPLEX_UNIT_DIP;
    private float textSize = 16; //dip值
    private boolean isAutoFit = true; //默认是自适应

    private OnPagerTitleClickListenr mOnPagerTitleClickListenr;

    public MySimpleNavigatorAdapter(ArrayList<String> mTitles, OnPagerTitleClickListenr mOnPagerTitleClickListenr) {
        this.mTitles = mTitles;
        this.mOnPagerTitleClickListenr = mOnPagerTitleClickListenr;
    }

    /**
     * 设置切换到的tab的文字大小。
     *
     * @param type
     * @param size
     */
    public void setTextSize(int type, float size) {
        setTextSize(type, size, false);
    }

    public void setTextSize(int type, float size, boolean fit) {
        this.typeValue = type;
        this.textSize = size;
        this.isAutoFit = fit;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        MySimplePagerTitleView simplePagerTitleView = new MySimplePagerTitleView(context);
        simplePagerTitleView.setText(mTitles.get(index));
        simplePagerTitleView.setTextSize(typeValue, textSize);
        simplePagerTitleView.setSizeToFit(isAutoFit);

        simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.hx_text_dark_black));
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.hx_golden_yellow));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPagerTitleClickListenr != null) {
                    mOnPagerTitleClickListenr.onPagerTitleClick(v, index);
                    mHXLinePagerIndicator.onPageSelected(index);
                    mHXLinePagerIndicator.onPageScrolled(index, 0, 0);
                }
            }
        });

        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        MyLinePagerIndicator indicator = new MyLinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(AppUtil.dip2px(context, 3));
        indicator.setRoundRadius(AppUtil.dip2px(context, 3));
        indicator.setLineWidth(AppUtil.dip2px(context, 30));

        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));

        mHXLinePagerIndicator = indicator;

        return indicator;
    }

    public interface OnPagerTitleClickListenr {
        void onPagerTitleClick(View view, int index);
    }
}
