package com.deych.cookchooser.models;

import com.deych.cookchooser.ui.meals.MealsActivity;
import com.deych.cookchooser.ui.meals.MealsListFragment;

import dagger.Subcomponent;

/**
 * Created by deigo on 20.12.2015.
 */
@UserScope
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    void inject(MealsActivity mealsActivity);
    void inject(MealsListFragment fragment);
}
