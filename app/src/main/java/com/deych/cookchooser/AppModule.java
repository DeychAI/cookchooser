package com.deych.cookchooser;

import android.content.Context;

import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.ui.base.PresenterCache;
import com.deych.cookchooser.util.RxSchedulerFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 17.12.2015.
 */
@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public App provideApp() {
        return app;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return app;
    }

    @Singleton
    @Provides
    public PresenterCache providePresenterCache(Preferences preferences) {
        return new PresenterCache(preferences);
    }

    @Singleton
    @Provides
    public Preferences providePreferences(Context context) {
        return new Preferences(context);
    }

    @Singleton
    @Provides
    public RxSchedulerFactory provideRxSchedulerFactory() {
        return new RxSchedulerFactory();
    }
}
