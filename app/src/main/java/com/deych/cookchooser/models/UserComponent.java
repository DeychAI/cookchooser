package com.deych.cookchooser.models;

import com.deych.cookchooser.ui.MainActivity;

import dagger.Subcomponent;

/**
 * Created by deigo on 20.12.2015.
 */
@UserScope
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    void inject(MainActivity mainActivity);
}
