package com.company.project.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CountUtil {
    /**
     * 秒级倒计时
     *
     * @param number   时长
     * @param observer 观察者
     */
    public static void numberDown(int number, CountObserver observer) {
        numberDown(number, 0, 1, TimeUnit.SECONDS, observer);
    }

    /**
     * 秒级倒计数
     *
     * @param number   区间上沿
     * @param step     单步距离
     * @param observer 观察者
     */
    public static void numberDown(int number, int step, CountObserver observer) {
        numberDown(number, 0, step, TimeUnit.SECONDS, observer);
    }

    /**
     * 倒计数器
     *
     * @param number   区间上沿
     * @param step     单步距离
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static void numberDown(int number, int step, TimeUnit unit, CountObserver observer) {
        numberDown(number, 0, step, unit, observer);
    }

    /**
     * 倒计数器
     *
     * @param top      区间上沿
     * @param bottom   区间下沿
     * @param step     单步距离
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static void numberDown(int top, int bottom, int step, TimeUnit unit, CountObserver observer) {
        Observable.interval(0, step, unit)//设置0延迟，每隔一秒发送一条数据
                .take((top - bottom) % step == 0 ? (top - bottom) / step : ((top - bottom) / step) + 1) //设置循环time/step次(无法整除则加1)
                .map(aLong -> (long) (//不低于0
                        (top - aLong.intValue() * step) < bottom ? bottom : (top - aLong.intValue() * step))
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 秒级计数器
     *
     * @param number   区间上沿
     * @param observer 观察者
     */
    public static void numberUp(int number, CountObserver observer) {
        numberUp(number, 0, 1, TimeUnit.SECONDS, observer);
    }

    /**
     * 秒级计数器
     *
     * @param number   区间上沿
     * @param step     单步距离
     * @param observer 观察者
     */
    public static void numberUp(int number, int step, CountObserver observer) {
        numberUp(number, 0, step, TimeUnit.SECONDS, observer);
    }

    /**
     * 计数器
     *
     * @param number   区间上沿
     * @param step     单步距离
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static void numberUp(int number, int step, TimeUnit unit, CountObserver observer) {
        numberUp(number, 0, step, unit, observer);
    }

    /**
     * 计数器
     *
     * @param top      区间上沿
     * @param bottom   区间下沿
     * @param step     单步距离
     * @param unit     时间单位
     * @param observer 观察者
     */
    public static void numberUp(int top, int bottom, int step, TimeUnit unit, CountObserver observer) {
        Observable.interval(0, step, unit)//设置0延迟，每隔一秒发送一条数据
                .take((top - bottom) % step == 0 ? (top - bottom) / step : ((top - bottom) / step) + 1) //设置循环time/step次(无法整除则加1)
                .map(aLong -> (bottom + aLong * step) > top ? top : (bottom + aLong * step))//不超过最大值
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
