package com.deych.cookchooser;

import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.ui.login.LoginFragment;
import com.deych.cookchooser.ui.login.RegisterFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
@Component(modules = { AppModule.class, NetModule.class})
public interface AppComponent {
    LoginFragment.LoginFragmentComponent plus(LoginFragment.LoginFragmentModule aFragmentModule);
    RegisterFragment.FragmentComponent plus(RegisterFragment.FragmentModule aFragmentModule);
}
