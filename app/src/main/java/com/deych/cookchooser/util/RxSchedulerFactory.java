package com.deych.cookchooser.util;

import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 23.01.2016.
 */
public class RxSchedulerFactory {

    public Scheduler io() {
        return Schedulers.io();
    }

    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }
}
