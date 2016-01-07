package com.deych.cookchooser.api;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.api.service.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by deigo on 20.12.2015.
 */
@Module
public class ApiModule {

    @Singleton
    @Provides
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }
}
