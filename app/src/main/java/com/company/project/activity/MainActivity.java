package com.company.project.activity;

import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.company.project.R;
import com.company.project.base.BaseActivity;
import com.company.project.base.IBasePresenter;

/**
 * @author EDZ
 * <p>
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    private FrameLayout fragmentHolder;
    private RadioGroup rgBottom;

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
        fragmentHolder = findViewById(R.id.flFragmentHolder);
        rgBottom = findViewById(R.id.rgMainBottom);
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
