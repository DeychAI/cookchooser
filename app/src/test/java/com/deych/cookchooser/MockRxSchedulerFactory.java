package com.deych.cookchooser;

import com.deych.cookchooser.util.RxSchedulerFactory;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 23.01.2016.
 */
public class MockRxSchedulerFactory extends RxSchedulerFactory{
    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler mainThread() {
        return Schedulers.immediate();
    }
}
