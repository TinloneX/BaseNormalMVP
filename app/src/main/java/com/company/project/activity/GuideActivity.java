package com.company.project.activity;


import androidx.viewpager.widget.ViewPager;

import com.company.project.R;
import com.company.project.R2;
import com.company.project.adapter.GuideAdapter;
import com.company.project.base.BaseActivity;
import com.company.project.base.IPresenter;

import butterknife.BindView;

/**
 * @author Tinlone
 * Let this be my last word, that I trust in thy love.
 */
public class GuideActivity extends BaseActivity {

    @BindView(R2.id.vpGuide)
    ViewPager vpGuide;

    @Override
    public int layoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        GuideAdapter guideAdapter = new GuideAdapter(this);
        vpGuide.setAdapter(guideAdapter);
    }

    @Override
    protected void initView() {
        super.initView();
        statusTransparentFontWhite();
    }
}
