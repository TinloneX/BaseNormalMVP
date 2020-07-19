package com.company.project.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tinlone
 * @date 2018/3/23.
 * The little flower lies in the dust. It sought the path of the butterfly.
 */

public abstract class BaseFragment<P extends IPresenter, DATA> extends Fragment implements IView<DATA> {
    public static final String FULL_SCREEN = "full_screen";
    public static final String AUTO_TITLE = "auto_title";
    public static final String RIGHT_TEXT = "right_text";
    public static final String TITLE = "title";
    public static final String URL = "url";

    protected P mPresenter;
    protected Context mContext;
    protected ViewGroup mRootView;
    protected boolean hasLoadData = false;
    /**
     * 限制666ms内多次跳转同一界面
     */
    private long lastClick = 0L;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(layoutId(), container, false);
        }
        if (useButterKnife()) {
            unbinder = ButterKnife.bind(this, mRootView);
        }
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView();
        initData();
        return mRootView;
    }

    protected boolean useButterKnife() {
        return isUseButterKnife();
    }

    public boolean isUseButterKnife(){
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
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
     * 设置title样式
     *
     * @param title title文字
     */
    protected void setTitle(String title) {
        setTitle(0, title, "", 0, true);
    }

    protected void setTitle(String title, boolean white) {
        setTitle(0, title, "", 0, white);
    }

    /**
     * 设置title样式
     *
     * @param title title文字
     */
    protected void setTitle(@StringRes int title) {
        setTitle(mContext.getResources().getString(title));
    }

    protected void setTitle(@StringRes int title, boolean white) {
        setTitle(mContext.getResources().getString(title), white);
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

    protected void setTitleBack(String title, String right, boolean white) {
        setTitle(R.mipmap.back, title, right, 0, white);
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
            RelativeLayout statusBar = mRootView.findViewById(R.id.rl_title);
            ImageView ivLeft = mRootView.findViewById(R.id.iv_back);
            ImageView ivRight = mRootView.findViewById(R.id.iv_title_right);
            TextView tvTitle = mRootView.findViewById(R.id.tv_title_text);
            TextView tvRight = mRootView.findViewById(R.id.tv_title_right);
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
            if (white) {
                statusBar.setBackgroundColor(Color.WHITE);
                tvTitle.setTextColor(Color.BLACK);
                tvRight.setTextColor(Color.BLACK);
            } else {
                statusBar.setBackgroundColor(Color.TRANSPARENT);
                tvTitle.setTextColor(Color.WHITE);
                tvRight.setTextColor(Color.WHITE);
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

    }


    /**
     * 打开新界面
     *
     * @param clazz 界面类
     *              DEBUG 修复笔误 BaseFragment -> BaseActivity
     */
    public void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle 数据
     *               DEBUG 修复笔误 BaseFragment -> BaseActivity
     */
    public void startActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        if (noDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(mContext, clazz);
            if (bundle == null) {
                bundle = new Bundle();
            }
            intent.putExtras(bundle);
            lastClick = System.currentTimeMillis();
            startActivity(intent);
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

    /**
     * 打开加载H5页面
     *
     * @param url        URL
     * @param title      TITLE
     * @param autoTitle  自动获取title
     * @param rightText  右侧标题字符
     * @param fullScreen 全屏？
     */
    public void openWebsite(String url, String title, boolean autoTitle, String rightText, boolean fullScreen) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TITLE, title);
        bundle.putString(RIGHT_TEXT, rightText);
        bundle.putBoolean(FULL_SCREEN, fullScreen);
        bundle.putBoolean(AUTO_TITLE, autoTitle);
        startActivity(WebsiteActivity.class, bundle);
    }

    public void startActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        if (noDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(mContext, clazz);
            if (bundle == null) {
                bundle = new Bundle();
            }
            intent.putExtras(bundle);
            lastClick = System.currentTimeMillis();
            startActivityForResult(intent, requestCode);
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
//                startActivity(SignInActivity.class);
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
        LoadingProgressDialog.showProgressDialog(mContext);
    }

    public void showLoading(String tips) {
        LoadingProgressDialog.showProgressDialog(mContext, tips);
    }

    public void showLoading(@StringRes int tips) {
        LoadingProgressDialog.showProgressDialog(mContext, getString(tips));
    }

    public void updateLoading(String tips) {
        LoadingProgressDialog.updateTips(mContext, tips);
    }

    public void updateLoading(@StringRes int tips) {
        LoadingProgressDialog.updateTips(mContext, getString(tips));
    }

    public void showLoading(@StringRes int tips, boolean canceled) {
        LoadingProgressDialog.showProgressDialog(mContext, getString(tips), canceled);
    }

    public void hideLoading() {
        LoadingProgressDialog.dismissProgressDialog();
    }

    protected boolean noDoubleClick() {
        return System.currentTimeMillis() - lastClick >= Config.Numbers.CLICK_LIMIT;
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        if (mContext instanceof BaseActivity) {
            WindowManager.LayoutParams lp = ((BaseActivity) mContext).getWindow().getAttributes();
            lp.alpha = bgAlpha;
            ((BaseActivity) mContext).getWindow().setAttributes(lp);
        }
    }

    protected void dismiss(@Size(min = 1) Dismissable... dialogs) {
        for (Dismissable dialog : dialogs) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

}
