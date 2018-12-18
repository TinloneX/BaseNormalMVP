package com.company.project.mvp;

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
     *
     * @param resultMsg  失败返回信息
     * @param resultCode 失败返回码
     */
    void onLoadFail(String resultMsg, String resultCode);

}
