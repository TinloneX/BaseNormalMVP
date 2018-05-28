package com.company.project.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.company.project.R;
import com.company.project.base.BaseActivity;
import com.company.project.bean.AdvertisementBean;
import com.company.project.bean.UserInfoBean;
import com.company.project.config.GlideApp;
import com.company.project.mvp.contract.AdvertisementContract;
import com.company.project.mvp.presenter.AdvertisementPresenter;
import com.company.project.util.Check;
import com.company.project.util.SharedPreferencesUtil;
import com.company.project.util.TLog;
import com.company.project.util.UserInfoUtil;
import com.company.project.MyApplication;

import java.lang.ref.WeakReference;

/**
 * @author EDZ
 *         启动广告页
 */
public class LauncherActivity extends BaseActivity<AdvertisementContract.IAdvertisementPresenter, AdvertisementBean> implements AdvertisementContract.IAdvertisementView {

    TextView tvSkip;
    ImageView ivAdvertisement;
    ImageView ivBottomLogo;
    private int time = 3;
    private MyHandler myHandler;

    @Override
    public int layoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initView() {
        ivBottomLogo = findViewById(R.id.ivBottomLogo);
        ivAdvertisement = findViewById(R.id.ivAdvertisement);
        tvSkip = findViewById(R.id.tvSkip);
    }

    @Override
    protected void initData() {
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessage(0);
        mPresenter.getAdvertisement();
    }

    @Override
    public AdvertisementContract.IAdvertisementPresenter getPresenter() {
        TLog.i("(LauncherActivity.java:57) -> getPresenter");
        return new AdvertisementPresenter();
    }

    @Override
    public void onLoadData(AdvertisementBean resultData) {
        if (resultData != null && Check.isLegalWebSite(resultData.getPicture())) {
            GlideApp.with(this)
                    .load(resultData.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivAdvertisement);
        }
    }

    @Override
    public void onLoadFail(String resultMsg, String resultCode) {

    }

    private void doNext() {
        int spVersionCode = (int) SharedPreferencesUtil.getParam(MyApplication.getAppContext(), SharedPreferencesUtil.VERSION_CODE, -1);
        int currentVersionCode = 0;
        PackageManager pm;
        try {
            pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            currentVersionCode = pi.versionCode;
        } catch (Exception ignored) {

        }

        UserInfoBean bean = UserInfoUtil.getUserInfo();
        bean.setFrom("android");
        bean.setVersionCode(currentVersionCode);
        UserInfoUtil.updateUserInfo(bean);

        if (currentVersionCode > spVersionCode) {
            SharedPreferencesUtil.setParam(MyApplication.getAppContext(), SharedPreferencesUtil.VERSION_CODE, currentVersionCode);
            startActivity(GuideActivity.class);
        } else {
//            startActivity(MainActivity.class);
            startActivity(TabIndicatorActivity.class);
        }

        this.finish();
    }

    private static class MyHandler extends Handler {

        WeakReference<LauncherActivity> weakActivity;

        private MyHandler(LauncherActivity activity) {
            this.weakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakActivity.get().time > 0) {
                weakActivity.get().tvSkip.setText(String.format("跳过%s", weakActivity.get().time));
                weakActivity.get().time--;
                weakActivity.get().myHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                weakActivity.get().doNext();
            }
        }
    }
}
