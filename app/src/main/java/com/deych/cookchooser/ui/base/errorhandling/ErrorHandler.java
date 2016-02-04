package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.ui.base.views.NetworkErrorView;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;


/**
 * Created by deigo on 26.01.2016.
 */
public class ErrorHandler<V extends NetworkErrorView> {

    private CaseHandler<V> caseHandler;

    public ErrorHandler(CaseHandler<V> caseHandler) {
        this.caseHandler = caseHandler;
    }

    public Resolver<V> handle(Throwable t) {
        if (t instanceof IOException) {
            return new IOExceptionResolver<>();
        } else if (t instanceof HttpException) {
            return caseHandler.handle((HttpException) t);
        }
        throw new RuntimeException(t);
    }
}
