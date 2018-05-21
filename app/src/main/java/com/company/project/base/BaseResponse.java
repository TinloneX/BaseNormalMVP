package com.company.project.base;

/**
 * @author EDZ
 * @date 2018/3/26.
 */

public class BaseResponse<T> {


    /**
     * resultCode : 0
     * resultTime : 20180326134451
     * resultData：{}
     */

    private String resultCode;
    private T resultData;
    private String resultTime;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultCode='" + resultCode + '\'' +
                ", resultData=" + resultData +
                ", resultTime='" + resultTime + '\'' +
                '}';
    }
}
