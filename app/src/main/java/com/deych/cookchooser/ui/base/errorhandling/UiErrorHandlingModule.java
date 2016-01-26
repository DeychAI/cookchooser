package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.ui.base.errorhandling.login.LoginViewCaseHandler;
import com.deych.cookchooser.ui.base.errorhandling.register.RegisterViewCaseHandler;
import com.deych.cookchooser.ui.login.LoginView;
import com.deych.cookchooser.ui.login.RegisterView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 26.01.2016.
 */
@Module
public class UiErrorHandlingModule {

    @Provides
    @Singleton
    public LoginViewCaseHandler provideLoginViewCaseHandler() {
        return new LoginViewCaseHandler();
    }

    @Provides
    @Singleton
    public ErrorHandler<LoginView> provideLoginErrorHandler(LoginViewCaseHandler caseHandler) {
        return new ErrorHandler<>(caseHandler);
    }

    @Provides
    @Singleton
    public RegisterViewCaseHandler provideRegisterViewCaseHandler() {
        return new RegisterViewCaseHandler();
    }

    @Provides
    @Singleton
    public ErrorHandler<RegisterView> provideRegisterErrorHandler(RegisterViewCaseHandler caseHandler) {
        return new ErrorHandler<>(caseHandler);
    }
}
