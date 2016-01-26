package com.deych.cookchooser.ui.base.errorhandling.register;

import com.deych.cookchooser.ui.base.errorhandling.AbstractHttpCodeResolver;
import com.deych.cookchooser.ui.login.RegisterView;

/**
 * Created by deigo on 26.01.2016.
 */
public class RegisterViewResolver extends AbstractHttpCodeResolver<RegisterView> {

    public RegisterViewResolver(int code) {
        super(code);
    }

    @Override
    protected boolean tryResolve(RegisterView view) {
        if (code == 409) {
            view.showUserExistsError();
            return true;
        }
        return false;
    }
}
