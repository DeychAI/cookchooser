package com.deych.cookchooser.ui.meals;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;

import java.util.List;

/**
 * Created by deigo on 20.12.2015.
 */
public interface MealsHostView {
    void showCategories(List<Category> categories);
    void showOneMeal(Meal meal);
    void showFullMeals(List<Meal> meals);
}
