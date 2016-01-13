package com.deych.cookchooser.ui.meals.add;

import com.deych.cookchooser.db.entities.Category;

import java.util.List;

/**
 * Created by alexanderdeych on 13.01.16.
 */
public interface AddMealView{
    void setCategories(List<Category> categories, long currentCategoryId);
    void setCurrentColor(/*TODO Color Enum here*/);
}
