package com.company.project.base;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.company.project.http.HttpClient;
import com.company.project.http.HttpObserver;
import com.company.project.http.HttpService;
import com.company.project.mvp.IModel;
import com.company.project.util.Tog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Tinlone
 * @date 2018/3/23.
 * Life has become richer by the love that has been lost.
 */

public class BaseModel implements IModel {

    protected HttpService mService = HttpClient.getInstance().getApiService();
    private SparseArray<Observable> queue = null;
    private int index = 0;

    @SuppressLint("CheckResult")
    protected Observable bindObservable(@NonNull Observable call) {
        if (queue == null) {
            queue = new SparseArray<>();
        }
        queue.append(index, call);
        index++;
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> void bindObservable(@NonNull Observable<BaseResponse<T>> call, @NonNull AsyncCallBack<BaseResponse<T>> callBack) {
        if (queue == null) {
            queue = new SparseArray<>();
        }
        queue.append(index, call);
        index++;
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpObserver.getInstance().createObserver(callBack));
    }

    @Override
    @SuppressLint("CheckResult")
    public void cancelRequest() {
        if (queue != null && queue.size() > 0) {
            for (int i = 0; i < queue.size(); i++) {
                Tog.i(queue.get(i).unsubscribeOn(AndroidSchedulers.mainThread()));
            }
            queue.clear();
            index = 0;
        }
    }
}
