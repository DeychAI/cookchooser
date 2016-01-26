package com.deych.cookchooser.ui.base.errorhandling.login;

import com.deych.cookchooser.ui.base.errorhandling.CaseHandler;
import com.deych.cookchooser.ui.base.errorhandling.Resolver;
import com.deych.cookchooser.ui.login.LoginView;

import retrofit2.HttpException;

/**
 * Created by deigo on 26.01.2016.
 */
public class LoginViewCaseHandler implements CaseHandler<LoginView> {
    @Override
    public Resolver<LoginView> handle(HttpException e) {
        return new LoginViewResolver(e.code());
    }
}
