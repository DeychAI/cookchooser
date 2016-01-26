package com.deych.cookchooser;

import com.deych.cookchooser.api.ApiModule;
import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;
import com.deych.cookchooser.ui.base.errorhandling.UiErrorHandlingModule;
import com.deych.cookchooser.user_scope.UserComponent;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.user_scope.UserModule;
import com.deych.cookchooser.ui.base.PresenterCache;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.login.LoginFragment;
import com.deych.cookchooser.ui.login.RegisterFragment;

import javax.inject.Singleton;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
@dagger.Component(modules = {AppModule.class,
        NetModule.class,
        DbModule.class,
        ApiModule.class,
        UiErrorHandlingModule.class
    }
)
public interface AppComponent {

    UserComponent plus(UserModule userModule);

    LoginFragment.LoginFragmentComponent plus(LoginFragment.LoginFragmentModule aModule);
    RegisterFragment.RegisterFragmentComponent plus(RegisterFragment.RegisterFragmentModule aModule);

    UserModel userModel();
}
