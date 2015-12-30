package com.deych.cookchooser;

import android.content.Context;

import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.ui.base.PresenterCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 17.12.2015.
 */
@Module
public class AppModule {

    private App mApp;

    public AppModule(App aApp) {
        mApp = aApp;
    }

    @Singleton
    @Provides
    public App provideApp() {
        return mApp;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return mApp;
    }

    @Singleton
    @Provides
    public PresenterCache providePresenterCache(Preferences preferences) {
        return new PresenterCache(preferences);
    }

    @Singleton
    @Provides
    public Preferences providePreferences(Context aContext) {
        return new Preferences(aContext);
    }
}
