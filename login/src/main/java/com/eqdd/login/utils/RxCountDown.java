package com.eqdd.login.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by lvzhihao on 17-5-31.
 */

public class RxCountDown {


    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(increaseTime->
                         countTime - increaseTime.intValue()
                   )
                .take(countTime + 1);

    }
}
