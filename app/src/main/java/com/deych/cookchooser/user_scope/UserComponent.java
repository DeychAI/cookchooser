package com.deych.cookchooser.user_scope;

import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.meals.detail.MealsDetailFragment;
import com.deych.cookchooser.ui.meals.MealsHostFragment;
import com.deych.cookchooser.ui.meals.MealsListFragment;

import dagger.Subcomponent;

/**
 * Created by deigo on 20.12.2015.
 */
@UserScope
@Subcomponent(modules = {UserModule.class, UserScopeApiModule.class})
public interface UserComponent {
    void inject(MealsHostFragment mealsHostFragment);
    void inject(MealsListFragment fragment);
    void inject(MealsDetailFragment fragment);
    void inject(MainActivity activity);
}