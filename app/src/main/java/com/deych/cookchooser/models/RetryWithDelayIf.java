package com.deych.cookchooser.models;

import android.util.Pair;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by deigo on 20.12.2015.
 */
public class RetryWithDelayIf implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int mMaxRetries;
    private final int mRetryDelay;
    private TimeUnit mTimeUnit;
    private Func1<Throwable, Boolean> mRetryIf;

    public RetryWithDelayIf(final int maxRetries, final int retryDelay, TimeUnit timeUnit, Func1<Throwable, Boolean> retryIf) {
        this.mMaxRetries = maxRetries;
        this.mRetryDelay = retryDelay;
        mTimeUnit = timeUnit;
        this.mRetryIf = retryIf;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.zipWith(Observable.range(1, mMaxRetries + 1), (n, i) -> {
            return new Pair<>(n, i);
        })
                .flatMap(
                        ni -> {
                            if (mRetryIf.call(ni.first) && ni.second <= mMaxRetries) {
                                return Observable.timer((long) ni.second * mRetryDelay, mTimeUnit);
                            } else {
                                return Observable.error(ni.first);
                            }
                        });
    }

}