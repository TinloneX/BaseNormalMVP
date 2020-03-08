package com.company.project.mvp;

import com.company.project.base.BaseResponse;

/**
 * @author Tinlone
 * @date 2018/3/23.
 */

public interface IView<Data> {
    /**
     * 获取到数据
     *
     * @param resultData 数据
     */
    void onLoadData(Data resultData);

    /**
     * 加载数据失败
     */
    void onLoadFail(BaseResponse response);

}
