package com.company.project.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.company.project.R;
import com.company.project.base.BaseActivity;
import com.company.project.base.IBasePresenter;
import com.company.project.fragment.FragmentOne;
import com.company.project.widget.indicator.MyCommonNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

public class TabIndicatorActivity extends BaseActivity {

    private String[] titles = new String[]{"一", "二", "三", "四", "五"};
    private List<Fragment> fragments = new ArrayList<>();

    private MagicIndicator magicIndicator;
    private ViewPager viewPager;

    @Override
    public int layoutId() {
        return R.layout.activity_tab_indicator;
    }

    @Override
    public IBasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onLoadData(Object resultData) {

    }

    @Override
    protected void initView() {
        super.initView();
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {
        super.initData();
        initTable(viewPager);
    }

    private void initTable(ViewPager viewPager) {
        for (int i = 0; i < titles.length; i++) {
            fragments.add(FragmentOne.newInstance());
        }
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        CommonNavigator commonNavigator = new CommonNavigator(getApplicationContext());
        commonNavigator.setAdjustMode(true);
        MyCommonNavigatorAdapter commonNavigatorAdapter = new MyCommonNavigatorAdapter(viewPager, titles);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
        viewPager.setOffscreenPageLimit(titles.length);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
