package com.deych.cookchooser.ui.base.errorhandling.login;

import com.deych.cookchooser.ui.base.errorhandling.AbstractHttpCodeResolver;
import com.deych.cookchooser.ui.login.LoginView;

/**
 * Created by deigo on 26.01.2016.
 */
public class LoginViewResolver extends AbstractHttpCodeResolver<LoginView> {

    public LoginViewResolver(int code) {
        super(code);
    }

    @Override
    protected boolean tryResolve(LoginView view) {
        if (code == 401) {
            view.showInvalidCredentialsError();
            return true;
        }
        return false;
    }
}
