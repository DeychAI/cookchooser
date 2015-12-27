package com.deych.cookchooser.ui.meals;

import com.deych.cookchooser.db.entities.Category;

import java.util.List;

/**
 * Created by deigo on 20.12.2015.
 */
public interface MealsView {
    void showCategories(List<Category> categories);
    void finishIfNotAuthorized();
    void showLoginScreen();
    void bindUserData(String username, String name);
}
