package com.company.project.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.company.project.MyApplication;
import com.company.project.R;
import com.company.project.base.BaseActivity;
import com.company.project.bean.AdvertisementBean;
import com.company.project.bean.UserInfoBean;
import com.company.project.config.GlideApp;
import com.company.project.mvp.contract.AdvertisementContract;
import com.company.project.mvp.presenter.AdvertisementPresenter;
import com.company.project.util.Check;
import com.company.project.util.CountObserver;
import com.company.project.util.CountUtil;
import com.company.project.util.SharedPreferencesUtil;
import com.company.project.util.TLog;
import com.company.project.util.UserInfoUtil;

import io.reactivex.disposables.Disposable;

/**
 * @author EDZ
 * 启动广告页
 * Let life be beautiful like summer flowers and death like autumn leaves.
 */
public class LauncherActivity extends BaseActivity<AdvertisementContract.IAdvertisementPresenter, AdvertisementBean> implements AdvertisementContract.IAdvertisementView {

    TextView tvSkip;
    ImageView ivAdvertisement;
    ImageView ivBottomLogo;
    private int time = 4;
    private Disposable disposable;

    @Override
    public int layoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initView() {
        ivBottomLogo = findViewById(R.id.ivBottomLogo);
        ivAdvertisement = findViewById(R.id.ivAdvertisement);
        tvSkip = findViewById(R.id.tvSkip);
        CountUtil.countDown(time, new CountObserver() {

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer t) {
                tvSkip.setText(String.format("跳过%ss", t - 1 < 0 ? 0 : t - 1));
            }

            @Override
            public void onComplete() {
                doNext();
            }
        });
        tvSkip.setOnClickListener(v -> {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            doNext();
        });
    }

    @Override
    protected void initData() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }
}
