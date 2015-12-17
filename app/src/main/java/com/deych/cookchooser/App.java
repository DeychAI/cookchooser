package com.deych.cookchooser;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.deych.cookchooser.di.components.NetComponent;
import com.deych.cookchooser.di.modules.NetModule;

/**
 * Created by deigo on 13.12.2015.
 */
public class App extends Application {


//    private NetComponent mNetComponent;
    private AppComponent mAppComponent;


    public static App get(@NonNull Context aContext) {
        return (App) aContext.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule())
                .netModule(new NetModule("http://deych.myihor.ru/cookchooser/api/v1/"))
                .build();

//        mAppComponent = DaggerAppComponent.create();
    }

//    public NetComponent getNetComponent() {
//        return mNetComponent;
//    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
