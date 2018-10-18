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
    public void getAdvertisement(HashMap<String,Object> params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack) {
        bindObservable(mService.getAdvertisement(params), callBack);
    }
}
