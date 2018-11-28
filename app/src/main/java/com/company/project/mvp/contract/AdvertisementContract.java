package com.company.project.mvp.contract;

import com.company.project.base.BaseResponse;
import com.company.project.base.IPresenter;
import com.company.project.bean.AdvertisementBean;
import com.company.project.mvp.IModel;
import com.company.project.mvp.IView;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface AdvertisementContract {

    interface IAdvertisementView extends IView<AdvertisementBean> {

    }

    interface IAdvertisementPresenter extends IPresenter<IAdvertisementView> {
        /**
         * 获取启动页广告
         */
        void getAdvertisement();
    }

    interface IAdvertisementModel extends IModel {

        /**
         * 获取广告
         *
         * @param params   参数
         * @param callBack 回调
         */
        void getAdvertisement(HashMap<String, Object> params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack);
    }
}
