package com.company.project.mvp.presenter;

import com.company.project.BuildConfig;
import com.company.project.mvp.IModel;
import com.company.project.mvp.contract.AdvertisementContract;
import com.company.project.mvp.imodel.IAdvertisementModel;
import com.company.project.mvp.model.AdvertisementModel;
import com.company.project.base.BasePresenter;
import com.company.project.base.BaseResponse;
import com.company.project.bean.AdvertisementBean;
import com.company.project.config.Config;
import com.company.project.util.Check;
import com.company.project.util.TLog;
import com.company.project.util.TypeCalculator;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public class AdvertisementPresenter extends BasePresenter<AdvertisementContract.IAdvertisementView, IAdvertisementModel> implements AdvertisementContract.IAdvertisementPresenter {

    @Override
    public void getAdvertisement() {

        HashMap<String, Object> params = new HashMap<>(16);
        params.put("from", "android");
        params.put("type", TypeCalculator.forScreenType());
        params.put("version", BuildConfig.VERSION_CODE);
        TLog.i("(AdvertisementPresenter.java:31) ->" + TLog.valueOf(params));

        mModel.getAdvertisement(params, new BaseAsyncCallback<BaseResponse<AdvertisementBean>>() {
            @Override
            public void onSuccess(BaseResponse<AdvertisementBean> resultData) {
                TLog.i("(AdvertisementPresenter.java:37) ->" + resultData.toString());
                if (Check.hasContent(resultData, mView)) {
                    mView.onLoadData(resultData.getResultData());
                }
            }
        });
    }

    @Override
    public void setModel() {
        mModel = new AdvertisementModel();
    }

}
