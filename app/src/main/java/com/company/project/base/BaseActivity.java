package com.company.project.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.R;
import com.company.project.activity.WebsiteActivity;
import com.company.project.config.Config;
import com.company.project.mvp.IView;
import com.company.project.util.Check;
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
    public static final String FULL_SCREEN = "full_screen";
    public static final String AUTO_TITLE = "auto_title";
    public static final String RIGHT_TEXT = "right_text";
    public static final String TITLE = "title";
    public static final String URL = "url";

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
        initParams(getIntent().getExtras());
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

    public void initParams(Bundle params) {
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

    protected void setTitleBack(String title) {
        setTitle(R.mipmap.back, title);
    }

    protected void setTitle(String title) {
        setTitle(0, title, "", 0);
    }

    protected void setTitleBack(String title, String right) {
        setTitle(R.mipmap.back, title, right, 0);
    }

    protected void setTitle(@DrawableRes int leftRes, String title) {
        setTitle(leftRes, title, "", 0);
    }

    protected void setTitle(@DrawableRes int leftRes, String title, String rightTitle, @DrawableRes int rightRes) {
        try {
            ImageView ivLeft = findViewById(R.id.iv_back);
            ImageView ivRight = findViewById(R.id.iv_title_right);
            TextView tvTitle = findViewById(R.id.tv_title_text);
            TextView tvRight = findViewById(R.id.tv_title_right);
            if (leftRes != 0) {
                ivLeft.setImageResource(leftRes);
            }
            if (rightRes != 0) {
                ivRight.setImageResource(rightRes);
            }
            ivLeft.setVisibility(leftRes != 0 ? View.VISIBLE : View.GONE);
            ivRight.setVisibility(rightRes != 0 ? View.VISIBLE : View.GONE);
            tvTitle.setText(title);
            tvRight.setText(rightTitle);
            tvRight.setVisibility(Check.hasContent(rightTitle) ? View.VISIBLE : View.GONE);
            ivLeft.setOnClickListener(view -> onTitleLeftClick());
            if (Check.hasContent(rightTitle))
                tvRight.setOnClickListener(view -> onTitleRightClick());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onTitleRightClick() {

    }

    /**
     * title左图标点击，默认关闭界面，可重写
     */
    protected void onTitleLeftClick() {
        closeSoftInput();
        finish();
    }

    protected void closeSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (inputMethodManager != null && currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    public void openWebsite(String url) {
        openWebsite(url, "", true, "", false);
    }

    public void openWebsite(String url, String title) {
        openWebsite(url, title, true, "", false);
    }

    public void openWebsite(String url, boolean fullScreen) {
        openWebsite(url, "", true, "", fullScreen);
    }

    public void openWebsite(String url, String title, boolean fullScreen) {
        openWebsite(url, title, false, "", fullScreen);
    }

    public void openWebsite(String url, boolean autoTitle, String rightText, boolean fullScreen) {
        openWebsite(url, "", autoTitle, rightText, fullScreen);
    }

    public void openWebsite(String url, String title, boolean autoTitle, String rightText, boolean fullScreen) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TITLE, title);
        bundle.putString(RIGHT_TEXT, rightText);
        bundle.putBoolean(FULL_SCREEN, fullScreen);
        bundle.putBoolean(AUTO_TITLE, autoTitle);
        startActivity(WebsiteActivity.class, bundle, true);
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
