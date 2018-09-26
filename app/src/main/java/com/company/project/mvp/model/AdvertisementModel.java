package com.company.project.mvp.model;

import com.company.project.base.BaseModel;
import com.company.project.base.BaseResponse;
import com.company.project.bean.AdvertisementBean;
import com.company.project.mvp.contract.AdvertisementContract;
import com.company.project.util.TLog;

import java.util.HashMap;


/**
 * @author EDZ
 * @date 2018/3/23.
 */

public class AdvertisementModel extends BaseModel implements AdvertisementContract.IAdvertisementModel {

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
