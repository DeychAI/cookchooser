package com.deych.cookchooser.ui.meals;

import com.deych.cookchooser.db.entities.Meal;

import java.util.List;

/**
 * Created by deigo on 20.12.2015.
 */
public interface MealsListView {
    void showMeals(List<Meal> meals);
    void hideRefresh();

}
