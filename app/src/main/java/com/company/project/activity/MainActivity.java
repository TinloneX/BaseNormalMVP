package com.company.project.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.chaychan.library.BottomBarLayout;
import com.company.project.R;
import com.company.project.adapter.PagerFragmentAdapter;
import com.company.project.base.BaseActivity;
import com.company.project.base.IBasePresenter;
import com.company.project.fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author EDZ
 * <p>
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_fragments)
    ViewPager vpFragments;
    @BindView(R.id.bottom_bar)
    BottomBarLayout bottomBar;

    private List<Fragment> fragments = new ArrayList<Fragment>() {
        {
            add(BlankFragment.newInstance(R.string.homepage, "#00B7EE"));
            add(BlankFragment.newInstance(R.string.info, "#EEB7EE"));
            add(BlankFragment.newInstance(R.string.discover, "#00EEB7"));
            add(BlankFragment.newInstance(R.string.mine, "#FFFACD"));
        }
    };

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public IBasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        vpFragments.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setViewPager(vpFragments);
        bottomBar.getBottomItem(0).setUnreadNum(10000);
        bottomBar.getBottomItem(2).setMsg("aloha");
        bottomBar.setOnItemSelectedListener((bottomBarItem, i, i1) -> {
            if (i1 == 2) {
                bottomBarItem.hideMsg();
            }
        });
    }

    @Override
    protected void initData() {
//        showLoading();
    }

    @Override
    public void onLoadData(Object resultData) {
        hideLoading();
    }

    @Override
    public void onLoadFail(String resultMsg, String resultCode) {
        hideLoading();
    }
}
