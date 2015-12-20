package com.deych.cookchooser;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.deych.cookchooser.api.ApiModule;
import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserComponent;
import com.deych.cookchooser.models.UserModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by deigo on 13.12.2015.
 */
public class App extends Application {

    private AppComponent mAppComponent;
    private UserComponent mUserComponent;

    public static App get(@NonNull Context aContext) {
        return (App) aContext.getApplicationContext();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher = LeakCanary.install(this);

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://deych.myihor.ru/cookchooser/api/v1/"))
                .dbModule(new DbModule())
                .apiModule(new ApiModule())
//                .netModule(new NetModule("http://deych.myihor.ru:5000/cookchooser/api/v1/"))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public UserComponent createUserComponent(User user) {
        mUserComponent = mAppComponent.plus(new UserModule(user));
        return mUserComponent;
    }

    public UserComponent getUserComponent() {
        return mUserComponent;
    }

    public void releaseUserComponent() {
        mUserComponent = null;
    }
}
