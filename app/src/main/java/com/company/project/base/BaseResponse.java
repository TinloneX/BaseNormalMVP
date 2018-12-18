package com.company.project.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author Tinlone
 * @date 2018/3/26.
 */

public class BaseResponse<T> {


    /**
     * resultCode : 0
     * message : 20180326134451
     * resultData：{}
     */
    @SerializedName("resultCode")
    private String resultCode;
    @SerializedName("resultData")
    private T resultData;
    @SerializedName("message")
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultCode='" + resultCode + '\'' +
                ", resultData=" + resultData +
                ", message='" + message + '\'' +
                '}';
    }
}
