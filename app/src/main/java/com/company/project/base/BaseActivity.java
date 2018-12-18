package com.company.project.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.R;
import com.company.project.config.Config;
import com.company.project.mvp.IView;
import com.company.project.widget.LoadingProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * @author Tinlone
 * @date 2018/3/23.
 * If you shed tears when you miss the sun, you also miss the stars.
 */

public abstract class BaseActivity<P extends IPresenter, DATA> extends AppCompatActivity implements IView<DATA> {
    protected P mPresenter;
    /**
     * 暴露出来供给单个界面更改样式
     */
    protected ImmersionBar immersionBar;
    /**
     * 限制666ms内多次跳转同一界面
     */
    private long lastClick = 0L;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initImmersionBar();
        unbinder = ButterKnife.bind(this);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            // 谁能帮我解决下类型问题~！
            mPresenter.attachView(this);
        }
        initView();
        initData();
    }

    /**
     * 沉浸式状态栏
     */
    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this)
                .keyboardEnable(true);
        immersionBar.init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * 状态栏白底黑字
     */
    public void statusWhiteFontBlack() {
        immersionBar
                .statusBarColor(R.color.white)
                .keyboardEnable(true)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    /**
     * 透明底黑字
     */
    public void statusTransparentFontBlack() {
        immersionBar
                .transparentStatusBar()
                .keyboardEnable(true)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    /**
     * 透明底白字
     */
    public void statusTransparentFontWhite() {
        immersionBar
                .transparentStatusBar()
                .fitsSystemWindows(false)
                .statusBarDarkFont(false)
                .keyboardEnable(true)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .init();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.dettachView();
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        lastClick = 0L;
        super.onDestroy();
    }

    /**
     * 获取布局id
     *
     * @return id
     */
    public abstract int layoutId();

    /**
     * 获取presenter
     *
     * @return presenter
     */
    public P getPresenter() {
        return null;
    }

    /**
     * 提供初始化控件入口
     */
    protected void initView() {
    }

    /**
     * 提供初始化数据入口
     * （非必选）
     */
    protected void initData() {

    }

    /**
     * 左侧进入
     */
    protected void leftStart() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 右侧进入
     */
    protected void rightStart() {
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 打开新界面
     *
     * @param clazz 界面类
     */
    public void startActivity(@NonNull Class<? extends BaseActivity> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(@NonNull Class<? extends BaseActivity> clazz, @Nullable Bundle bundle) {
        startActivity(clazz, bundle, true);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle 数据
     */
    public void startActivity(@NonNull Class<? extends BaseActivity> clazz, @Nullable Bundle bundle, boolean right) {
        if (noDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clazz);
            if (bundle == null) {
                bundle = new Bundle();
            }
            intent.putExtras(bundle);
            lastClick = System.currentTimeMillis();
            startActivity(intent);
            if (right) {
                rightStart();
            } else {
                leftStart();
            }
        }
    }

    /**
     * 成功响应
     *
     * @param resultData 数据
     */
    @Override
    public void onLoadData(DATA resultData) {
        hideLoading();
    }

    /**
     * 失败响应
     *
     * @param resultMsg  失败返回信息
     * @param resultCode 失败返回码
     */
    @Override
    public void onLoadFail(String resultMsg, String resultCode) {
        hideLoading();
        ToastUtils.showShort(resultMsg);
    }

    public void showLoading() {
        LoadingProgressDialog.showProgressDialog(this);
    }

    public void hideLoading() {
        LoadingProgressDialog.dismissProgressDialog();
    }

    /**
     * 防止抖动连点
     */
    protected boolean noDoubleClick() {
        return System.currentTimeMillis() - lastClick >= Config.Numbers.CLICK_LIMIT;
    }

    /**
     * 释放订阅者
     */
    protected void dispose(Disposable... disposables) {
        for (Disposable i : disposables) {
            if (i != null) {
                i.dispose();
            }
        }
    }
}
