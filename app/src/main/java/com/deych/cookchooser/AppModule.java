package com.deych.cookchooser;

import com.deych.cookchooser.ui.base.PresenterCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 17.12.2015.
 */
@Module
public class AppModule {

    @Singleton
    @Provides
    public PresenterCache providePresenterCache() {
        return new PresenterCache();
    }
}
