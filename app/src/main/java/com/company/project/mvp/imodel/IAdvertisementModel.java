package com.company.project.mvp.imodel;

import com.company.project.mvp.IModel;
import com.company.project.base.BaseResponse;
import com.company.project.bean.AdvertisementBean;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface IAdvertisementModel extends IModel {

    /**
     * 获取广告
     *
     * @param params   参数
     * @param callBack 回调
     */
    void getAdvertisement(HashMap params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack);
}
