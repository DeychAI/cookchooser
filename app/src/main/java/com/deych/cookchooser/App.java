package com.deych.cookchooser;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.deych.cookchooser.api.ApiModule;
import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.user_scope.UserComponent;
import com.deych.cookchooser.user_scope.UserModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;
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
        Fabric.with(this, new Crashlytics());

        refWatcher = LeakCanary.install(this);

        appComponent = prepareAppComponent().build();

        User user = appComponent.userModel().loginAsBlocking();
        if (user != null) {
            createUserComponent(user);
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    protected DaggerAppComponent.Builder prepareAppComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://deych.myihor.ru/cookchooser/api/v1/"))
                .dbModule(new DbModule())
                .apiModule(new ApiModule());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UserComponent createUserComponent(User user) {
        Crashlytics.setUserEmail(user.getUsername());
        Crashlytics.setUserName(user.getName());
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
