package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.ui.base.errorhandling.login.LoginViewCaseHandler;
import com.deych.cookchooser.ui.base.errorhandling.register.RegisterViewCaseHandler;
import com.deych.cookchooser.ui.base.views.NetworkErrorView;
import com.deych.cookchooser.ui.login.LoginView;
import com.deych.cookchooser.ui.login.RegisterView;

import java.io.IOException;

import retrofit2.HttpException;

/**
 * Created by deigo on 26.01.2016.
 */
public class ErrorHandler<V extends NetworkErrorView> {

    private CaseHandler<V> caseHandler;

    private ErrorHandler(CaseHandler<V> caseHandler) {
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

    public static ErrorHandler<LoginView> forLogin() {
        return new ErrorHandler<>(new LoginViewCaseHandler());
    }

    public static ErrorHandler<RegisterView> forRegister() {
        return new ErrorHandler<>(new RegisterViewCaseHandler());
    }
}
