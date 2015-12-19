package com.deych.cookchooser;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.deych.cookchooser.api.NetModule;
import com.deych.cookchooser.db.DbModule;

import timber.log.Timber;

/**
 * Created by deigo on 13.12.2015.
 */
public class App extends Application {

    private AppComponent mAppComponent;

    public static App get(@NonNull Context aContext) {
        return (App) aContext.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://deych.myihor.ru/cookchooser/api/v1/"))
                .dbModule(new DbModule())
//                .netModule(new NetModule("http://deych.myihor.ru:5000/cookchooser/api/v1/"))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
