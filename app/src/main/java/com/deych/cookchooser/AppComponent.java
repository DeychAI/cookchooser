package com.deych.cookchooser;

import com.deych.cookchooser.di.modules.NetModule;
import com.deych.cookchooser.ui.login.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
@Component(modules = { AppModule.class, NetModule.class})
public interface AppComponent {
    LoginFragment.LoginFragmentComponent plus(LoginFragment.LoginFragmentModule aLoginFragmentModule);
}
