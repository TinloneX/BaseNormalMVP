package com.company.project.activity;


import com.blankj.utilcode.util.ToastUtils;
import com.chaychan.library.BottomBarLayout;
import com.company.project.R;
import com.company.project.R2;
import com.company.project.adapter.PagerFragmentAdapter;
import com.company.project.base.BaseActivity;
import com.company.project.base.IPresenter;
import com.company.project.fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * @author Tinlone
 * <p>
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    @BindView(R2.id.vp_fragments)
    ViewPager vpFragments;
    @BindView(R2.id.bottom_bar)
    BottomBarLayout bottomBar;
    String msg = "";
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
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        vpFragments.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setViewPager(vpFragments);
        bottomBar.getBottomItem(0).setUnreadNum(10000);
        bottomBar.getBottomItem(2).setMsg("aloha");
        bottomBar.setOnItemSelectedListener((bottomBarItem, i, i1) -> {
            switch (i1) {
                case 0:
                    statusTransparentFontWhite();
                    msg = "透明状态栏白字";
                    break;
                case 1:
                    statusTransparentFontBlack();
                    msg = "透明状态栏黑字";
                    break;
                case 2:
                    bottomBarItem.hideMsg();
                    break;
                case 3:
                    statusWhiteFontBlack();
                    msg = "白底状态栏黑字";
                    break;
            }
            ToastUtils.showShort(msg);
        });
    }

}
