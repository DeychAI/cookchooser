package com.deych.cookchooser.util;

import android.util.Pair;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by deigo on 20.12.2015.
 */
public class RetryWithDelayIf implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelay;
    private TimeUnit timeUnit;
    private Func1<Throwable, Boolean> retryIf;

    public RetryWithDelayIf(final int maxRetries, final int retryDelay, TimeUnit timeUnit, Func1<Throwable, Boolean> retryIf) {
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
        this.timeUnit = timeUnit;
        this.retryIf = retryIf;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.zipWith(Observable.range(1, maxRetries + 1), (n, i) -> {
            return new Pair<>(n, i);
        })
                .flatMap(
                        ni -> {
                            if (retryIf.call(ni.first) && ni.second <= maxRetries) {
                                return Observable.timer((long) ni.second * retryDelay, timeUnit);
                            } else {
                                return Observable.error(ni.first);
                            }
                        });
    }

}