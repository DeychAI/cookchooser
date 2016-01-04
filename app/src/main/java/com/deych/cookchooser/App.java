package com.deych.cookchooser;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.deych.cookchooser.api.ApiModule;
import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.user_scope.UserComponent;
import com.deych.cookchooser.user_scope.UserModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by deigo on 13.12.2015.
 */
public class App extends Application {

    private AppComponent appComponent;
    private UserComponent userComponent;

    public static App get(@NonNull Context context) {
        return (App) context.getApplicationContext();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher = LeakCanary.install(this);

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://deych.myihor.ru/cookchooser/api/v1/"))
                .dbModule(new DbModule())
                .apiModule(new ApiModule())
//                .netModule(new NetModule("http://deych.myihor.ru:5000/cookchooser/api/v1/"))
                .build();

        User user = appComponent.userModel().loginAsBlocking();
        if (user != null) {
            createUserComponent(user);
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UserComponent createUserComponent(User user) {
        userComponent = appComponent.plus(new UserModule(user));
        return userComponent;
    }

    public UserComponent getUserComponent() {
        return userComponent;
    }

    public void releaseUserComponent() {
        userComponent = null;
    }
}
