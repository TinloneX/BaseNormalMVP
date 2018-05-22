package com.company.project.mvp.model;

import com.company.project.base.BaseModel;
import com.company.project.base.BaseResponse;
import com.company.project.bean.AdvertisementBean;
import com.company.project.http.HttpObserver;
import com.company.project.mvp.imodel.IAdvertisementModel;
import com.company.project.util.TLog;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public class AdvertisementModel extends BaseModel implements IAdvertisementModel {

    @Override
    public void getAdvertisement(HashMap params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack) {
        TLog.i("(AdvertisementModel.java:26) -> getAdvertisement" + mService);
        bindObservable(mService.getAdvertisement(params), callBack);
//        bindObservable(mService.getAdvertisement(params))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(HttpObserver.getInstance().createObserver(callBack));
    }
}
