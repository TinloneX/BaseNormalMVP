package com.company.project.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.R;
import com.company.project.activity.WebsiteActivity;
import com.company.project.config.Config;
import com.company.project.http.ApiCode;
import com.company.project.mvp.IView;
import com.company.project.util.ActivityStackUtils;
import com.company.project.util.Check;
import com.company.project.util.UserInfoUtil;
import com.company.project.widget.Dismissable;
import com.company.project.widget.LoadingProgressDialog;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * @author Tinlone
 *  2018/3/23.
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
        unbinder = ButterKnife.bind(this);
        initImmersionBar();
        statusWhiteFontBlack();
        pressThis(inAll());
        initParams(getIntent().getExtras());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView();
        initData();
    }

    protected boolean inAll() {
        return true;
    }

    protected void pressThis(boolean inAll) {
        if (inAll) {
            ActivityStackUtils.pressActivity(Config.Tags.ALL, this);
        }
    }


    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            //fontScale不为1，需要强制设置为1
            getResources();
        }
    }

    /**
     * 限制应用字体随系统改变
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) {
            //fontScale不为1，需要强制设置为1
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置成默认值，即fontScale为1
            createConfigurationContext(newConfig);
        }
        return resources;
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
                .fitsSystemWindows(false)
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
                .fitsSystemWindows(false)
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
        hideLoading();
        ActivityStackUtils.popActivity(Config.Tags.ALL, this);
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
    public abstract @LayoutRes
    int layoutId();

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

    /**
     * 设置title样式
     *
     * @param title title文字
     */
    protected void setTitleBack(String title) {
        setTitle(R.mipmap.back, title);
    }

    /**
     * 设置title样式
     *
     * @param title title文字
     */
    protected void setTitleBack(@StringRes int title) {
        setTitle(R.mipmap.back, getString(title));
    }

    /**
     * 设置title样式
     *
     * @param title title文字
     */
    protected void setTitle(String title) {
        setTitle(0, title, "", 0, true);
    }

    /**
     * 设置title样式
     *
     * @param title title文字
     * @param right 右侧文字
     */
    protected void setTitleBack(String title, String right) {
        setTitle(R.mipmap.back, title, right, 0, true);
    }

    /**
     * 设置title样式
     *
     * @param title title文字
     * @param right 右侧文字
     */
    protected void setTitleBack(int title, int right) {
        setTitle(R.mipmap.back, getString(title), getString(right), 0, true);
    }

    /**
     * 设置title样式
     *
     * @param leftRes 左侧资源
     * @param title   title文字
     */
    protected void setTitle(@DrawableRes int leftRes, String title) {
        setTitle(leftRes, title, "", 0, true);
    }

    /**
     * 设置title样式
     *
     * @param leftRes    左侧资源
     * @param title      title文字
     * @param rightTitle 右侧文字
     * @param rightRes   右侧资源
     */
    protected void setTitle(@DrawableRes int leftRes, String title, String rightTitle, @DrawableRes int rightRes, boolean white) {
        try {
            RelativeLayout statusBar = findViewById(R.id.rl_title);
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
            if (Check.hasContent(rightTitle)) {
                tvRight.setOnClickListener(view -> onTitleRightClick());
            }
            ivRight.setOnClickListener(v -> onTitleRightClick());
            if (white) {
                tvRight.setTextColor(Color.BLACK);
                tvTitle.setTextColor(Color.BLACK);
                statusBar.setBackgroundColor(Color.WHITE);
            } else {
                tvRight.setTextColor(Color.WHITE);
                tvTitle.setTextColor(Color.WHITE);
                statusBar.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 右侧点击事件
     */
    protected void onTitleRightClick() {

    }

    /**
     * title左图标点击，默认关闭界面，可重写
     */
    protected void onTitleLeftClick() {
        closeSoftInput();
        finish();
    }

    /**
     * 关闭软键盘
     */
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
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    /**
     * 右侧进入
     */
    protected void rightStart() {
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 打开新界面
     *
     * @param clazz 界面类
     */
    public void startActivity(@NonNull Class<? extends Activity> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle bundle数据
     */
    public void startActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        startActivity(clazz, bundle, true);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle 数据
     */
    public void startActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, boolean right) {
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

    public void startActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        if (noDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clazz);
            if (bundle == null) {
                bundle = new Bundle();
            }
            intent.putExtras(bundle);
            lastClick = System.currentTimeMillis();
            startActivityForResult(intent, requestCode);
            rightStart();
        }
    }

    /**
     * 打开网页界面
     *
     * @param url URL网址
     */
    public void openWebsite(String url) {
        openWebsite(url, "", true, "", false);
    }

    /**
     * 打开网页界面
     *
     * @param url   URL网址
     * @param title 标题文字
     */
    public void openWebsite(String url, String title) {
        openWebsite(url, title, true, "", false);
    }

    /**
     * 打开网页界面
     *
     * @param url        URL 网址
     * @param fullScreen 是否全屏
     */
    public void openWebsite(String url, boolean fullScreen) {
        openWebsite(url, "", true, "", fullScreen);
    }

    /**
     * 打开网页界面
     *
     * @param url        URL 网址
     * @param title      标题文字
     * @param fullScreen 是否全屏
     */
    public void openWebsite(String url, String title, boolean fullScreen) {
        openWebsite(url, title, false, "", fullScreen);
    }

    /**
     * 打开网页界面
     *
     * @param url        URL 网址
     * @param autoTitle  是否自动获取标题
     * @param rightText  右侧文字
     * @param fullScreen 是否全屏
     */
    public void openWebsite(String url, boolean autoTitle, String rightText, boolean fullScreen) {
        openWebsite(url, "", autoTitle, rightText, fullScreen);
    }

    /**
     * 打开网页界面
     *
     * @param url        URL 网址
     * @param title      标题文字
     * @param autoTitle  是否自动获取标题
     * @param rightText  右侧文字
     * @param fullScreen 是否全屏
     */
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
     */
    @Override
    public void onLoadFail(BaseResponse response) {
        hideLoading();
        if (response == null) {
            return;
        }
        switch (response.getResultCode()) {
            case ApiCode.HAS_NO_NETWORK:
                ToastUtils.showShort(response.getMessage());
                break;
            case ApiCode.TOKEN_EXPIRED:
            case ApiCode.TOKEN_INVALID:
                ToastUtils.showShort(R.string.sign_in_info_overdue_reload);
                UserInfoUtil.clearUserInfo();
                ActivityStackUtils.finishAll(Config.Tags.ALL);
                break;
            default:
                if (Check.hasContent(response.getMessage())) {
                    ToastUtils.showShort(response.getMessage());
                } else {
                    ToastUtils.showShort(R.string.request_failed_retry);
                }
                break;
        }
    }


    /**
     * 显示加载框，暂时未使用
     */
    @SuppressWarnings("unused")
    public void showLoading() {
        LoadingProgressDialog.showProgressDialog(this);
    }

    public void showLoading(String tips) {
        LoadingProgressDialog.showProgressDialog(this, tips);
    }

    public void showLoading(@StringRes int tips) {
        LoadingProgressDialog.showProgressDialog(this, getString(tips));
    }

    public void updateLoading(String tips) {
        LoadingProgressDialog.updateTips(this, tips);
    }

    public void updateLoading(@StringRes int tips) {
        LoadingProgressDialog.updateTips(this, getString(tips));
    }

    public void showLoading(@StringRes int tips, boolean canceled) {
        LoadingProgressDialog.showProgressDialog(this, getString(tips), canceled);
    }

    /**
     * 隐藏加载框，暂时未使用
     */
    @SuppressWarnings("unused")
    public void hideLoading() {
        LoadingProgressDialog.dismissProgressDialog();
    }

    /**
     * 防止抖动连点
     */
    protected boolean noDoubleClick() {
        return System.currentTimeMillis() - lastClick >= Config.Numbers.CLICK_LIMIT;
    }

    @Override
    public void onBackPressed() {
        if (!LoadingProgressDialog.isShowing()) {
            onTitleLeftClick();
        }
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

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        Window mWindow = getWindow();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        // 解决部分奇葩机型导致背景透明
        mWindow.addFlags(attributes.flags | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        attributes.flags = (attributes.flags | WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        attributes.alpha = bgAlpha;
        getWindow().setAttributes(attributes);
    }

    protected void dismiss(@Size(min = 1) Dismissable... dialogs) {
        for (Dismissable dialog : dialogs) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }


}
