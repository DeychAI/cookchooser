package com.deych.cookchooser.ui.meals.edit;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;

import java.util.List;

/**
 * Created by alexanderdeych on 13.01.16.
 */
public interface EditMealView {
    void setCategories(List<Category> categories, long selectedCategory);
    void setTitle(String title);
    void onSaved();
    void showError();

    void setMealData(Meal meal);
}
