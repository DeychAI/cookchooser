package com.deych.cookchooser.ui.meals.detail;

import com.deych.cookchooser.db.entities.Category;

import java.util.List;

/**
 * Created by deigo on 04.01.2016.
 */
public interface MealsDetailView {
    void showLoading();
    void hideLoading();
    void setCategories(List<Category> categories);
}
