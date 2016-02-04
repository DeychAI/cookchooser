package com.deych.cookchooser.ui.base.errorhandling.register;

import com.deych.cookchooser.ui.base.errorhandling.CaseHandler;
import com.deych.cookchooser.ui.base.errorhandling.Resolver;
import com.deych.cookchooser.ui.login.RegisterView;

import retrofit2.adapter.rxjava.HttpException;


/**
 * Created by deigo on 26.01.2016.
 */
public class RegisterViewCaseHandler implements CaseHandler<RegisterView> {
    @Override
    public Resolver<RegisterView> handle(HttpException e) {
        return new RegisterViewResolver(e.code());
    }
}
