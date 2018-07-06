package com.company.project.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.R;
import com.company.project.config.Config;
import com.company.project.mvp.IView;
import com.company.project.widget.LoadingProgressDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

/**
 * @author EDZ
 * @date 2018/3/23.
 * If you shed tears when you miss the sun, you also miss the stars.
 */

public abstract class BaseActivity<P extends IBasePresenter, V> extends AppCompatActivity implements IView<V> {
    protected P mPresenter;

    /**
     * 限制666ms内多次跳转同一界面
     */
    private long lastClick = 0L;
    /**
     * 暴露出来供给单个界面更改样式
     */
    protected ImmersionBar immersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initImmersionBar();
        mPresenter = getPresenter();
        if (mPresenter != null) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.dettachView();
        }
        lastClick = 0L;
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
    public abstract P getPresenter();

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
        overridePendingTransition(R.anim.push_left_in, 0);
    }

    /**
     * 右侧进入
     */
    protected void rightStart() {
        overridePendingTransition(R.anim.push_right_in, 0);
    }

    /**
     * 打开新界面
     *
     * @param clazz 界面类
     */
    public void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle 数据
     */
    public void startActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        if (System.currentTimeMillis() - lastClick < Config.Numbers.CLICK_LIMITED) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        lastClick = System.currentTimeMillis();
        startActivity(intent);
        rightStart();
    }

    /**
     * 成功响应
     *
     * @param resultData 数据
     */
    @Override
    public abstract void onLoadData(V resultData);

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

    @Override
    public void showLoading() {
        LoadingProgressDialog.showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        LoadingProgressDialog.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
