package com.deych.cookchooser.user_scope;

import com.deych.cookchooser.api.service.InvitesService;
import com.deych.cookchooser.api.service.MealsService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by deigo on 30.12.2015.
 */
@Module
public class UserScopeApiModule {

    @UserScope
    @Provides
    MealsService provideMealsService(@Named("RetrofitWithAuth") Retrofit retrofit) {
        return retrofit.create(MealsService.class);
    }

    @UserScope
    @Provides
    InvitesService provideInvitesService(@Named("RetrofitWithAuth") Retrofit retrofit) {
        return retrofit.create(InvitesService.class);
    }
}
