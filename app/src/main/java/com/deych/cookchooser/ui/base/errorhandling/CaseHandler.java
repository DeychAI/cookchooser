package com.deych.cookchooser.ui.base.errorhandling;


import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by deigo on 26.01.2016.
 */
public interface CaseHandler<V> {
    Resolver<V> handle(HttpException e);
}
