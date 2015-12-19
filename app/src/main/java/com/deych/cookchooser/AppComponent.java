package com.deych.cookchooser;

import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;
import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.login.LoginFragment;
import com.deych.cookchooser.ui.login.RegisterFragment;

import javax.inject.Singleton;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
@dagger.Component(modules = { AppModule.class, NetModule.class, DbModule.class})
public interface AppComponent {

    LoginFragment.LoginFragmentComponent plus(LoginFragment.LoginFragmentModule aModule);
    RegisterFragment.RegisterFragmentComponent plus(RegisterFragment.RegisterFragmentModule aModule);

    void inject(LoginActivity aActivity);
    void inject(MainActivity aMainActivity);
}
