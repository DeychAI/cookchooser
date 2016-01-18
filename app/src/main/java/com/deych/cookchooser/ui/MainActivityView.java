package com.deych.cookchooser.ui;

import com.deych.cookchooser.db.entities.MealColor;

/**
 * Created by deigo on 30.12.2015.
 */
public interface MainActivityView {
    void showLoginScreen();
    void bindUserData(String username, String name);
    void onColorSelected();
    void updateColorCount(MealColor color, int count);
    void selectColor(MealColor selectedColor);
}
