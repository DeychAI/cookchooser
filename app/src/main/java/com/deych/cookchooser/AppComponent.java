package com.deych.cookchooser;

import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.login.LoginFragment;
import com.deych.cookchooser.ui.login.RegisterFragment;

import javax.inject.Singleton;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
@dagger.Component(modules = { AppModule.class, NetModule.class})
public interface AppComponent {

    LoginFragment.Component plus(LoginFragment.Module aModule);
    RegisterFragment.Component plus(RegisterFragment.Module aModule);

    void inject(LoginActivity aActivity);
}
