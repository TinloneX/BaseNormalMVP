package com.company.project.http;

import com.company.project.base.BaseResponse;
import com.company.project.config.Config;
import com.company.project.mvp.IModel;
import com.company.project.util.TLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zjl
 * @date 2017/10/17 0017.
 */

public class HttpObserver {
    private static HttpObserver mHttpDao = null;

    private HttpObserver() {

    }

    public static HttpObserver getInstance() {
        if (mHttpDao == null) {
            mHttpDao = new HttpObserver();
        }
        return mHttpDao;
    }

    public <T> Observer<BaseResponse<T>> createObserver(final IModel.AsyncCallBack<BaseResponse<T>> callBack) {
        return new Observer<BaseResponse<T>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseResponse<T> response) {
                // 此处统一处理网络请求状态
                if (Config.Strings.RESPONSE_OK.equals(String.valueOf(response.getResultCode()))) {
                    callBack.onSuccess(response);
                } else {
                    callBack.onFailed(response);
                }
                TLog.i("tag", "(HttpObserver.java:45) ~ onNext:" + response.toString());
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFailed(null);
            }

            @Override
            public void onComplete() {

            }
        };

    }

}
