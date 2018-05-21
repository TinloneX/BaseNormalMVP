package com.company.project.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.company.project.mvp.IView;
import com.company.project.config.Config;
import com.company.project.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public abstract class BaseActivity<P extends IBasePresenter, V> extends AppCompatActivity implements IView<V> {
    protected P mPresenter;

    /**
     * 限制666ms内多次跳转同一界面
     */
    private long lastClick = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView();
        initData();
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
    public void onLoadFail(String resultMsg, String resultCode){
        hideLoading();
        ToastUtils.showShort(Config.Strings.SERVER_ERROR);
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
