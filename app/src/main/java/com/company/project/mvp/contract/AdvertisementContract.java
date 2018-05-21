package com.company.project.mvp.contract;

import com.company.project.base.IBasePresenter;
import com.company.project.mvp.IView;
import com.company.project.bean.AdvertisementBean;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface AdvertisementContract {

    interface IAdvertisementView extends IView<AdvertisementBean> {

    }

    interface IAdvertisementPresenter extends IBasePresenter<IAdvertisementView> {
        /**
         * 获取启动页广告
         */
        void getAdvertisement();
    }

}
