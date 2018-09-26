package com.company.project.base;

import com.company.project.BuildConfig;
import com.company.project.mvp.IModel;
import com.company.project.mvp.IView;
import com.company.project.util.Check;
import com.company.project.util.TLog;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public abstract class BasePresenter<V extends IView, M extends IModel> implements IBasePresenter<V> {

    protected M mModel = null;
    protected V mView = null;
    private HashMap<String, Object> baseParams;

    @Override
    public void attachView(V view) {
        mView = view;
        setModel();
    }

    protected HashMap<String, Object> getBaseParams(String token) {
        if (baseParams == null) {
            baseParams = new HashMap<>(16);
        } else {
            baseParams.clear();
        }
        if (Check.hasContent(token)) {
            baseParams.put("token", token);
        }
        baseParams.put("from", "android");
        baseParams.put("version", BuildConfig.VERSION_CODE);
        return baseParams;
    }

    public abstract void setModel();

    @Override
    public void dettachView() {
        if (mModel != null) {
            mModel.cancelRequest();
            mModel = null;
        }
        mView = null;
        TLog.i("(BasePresenter.java:31) -> dettachView");
    }

    protected abstract class BaseAsyncCallback<Data> implements IModel.AsyncCallBack<Data> {

        @Override
        public abstract void onSuccess(Data resultData);

        @Override
        public void onFailed(String resultMsg, String resultCode) {
            if (mView != null) {
                mView.onLoadFail(resultMsg, resultCode);
            }
        }
    }
}
