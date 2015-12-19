package com.deych.cookchooser.models;

import com.deych.cookchooser.db.entities.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 19.12.2015.
 */
@Module
public class UserModule {

    private User mUser;

    public UserModule(User user) {
        mUser = user;
    }

    @UserScope
    @Provides
    public User provideUser() {
        return mUser;
    }

}
