package com.deych.cookchooser.db.entities;

import com.deych.cookchooser.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deigo on 18.01.2016.
 */
public enum MealColor {

    @SerializedName("none")
    None("none", R.drawable.none_btn, R.id.nav_list, R.id.btnNone),
    @SerializedName("red")
    Red("red", R.drawable.red_btn, R.id.nav_list_red, R.id.btnRed),
    @SerializedName("green")
    Green("green", R.drawable.green_btn, R.id.nav_list_green, R.id.btnGreen),
    @SerializedName("blue")
    Blue("blue", R.drawable.blue_btn, R.id.nav_list_blue, R.id.btnBlue),
    @SerializedName("orange")
    Orange("orange", R.drawable.orange_btn, R.id.nav_list_orange, R.id.btnOrange);

    private final String color;
    private final int drawableRes;
    private final int menuRes;
    private int radioBtnRes;

    MealColor(String color, int drawableRes, int menuRes, int radioBtnRes) {
        this.color = color;
        this.drawableRes = drawableRes;
        this.menuRes = menuRes;
        this.radioBtnRes = radioBtnRes;
    }

    public String color() {
        return color;
    }

    public int drawableRes() {
        return drawableRes;
    }

    public int menuRes() {
        return menuRes;
    }

    public int radioBtnRes() {
        return radioBtnRes;
    }

    public static MealColor fromStringColor(String color) {
        if (color != null) {
            for (MealColor mealColor : MealColor.values()) {
                if (color.equalsIgnoreCase(mealColor.color())) {
                    return mealColor;
                }
            }
        }
        throw new IllegalArgumentException("Bad color");
    }

    public static MealColor fromRadioBtn(int radioBtnRes) {
        for (MealColor mealColor : MealColor.values()) {
            if (radioBtnRes == mealColor.radioBtnRes()) {
                return mealColor;
            }
        }
        throw new IllegalArgumentException("Bad color");
    }
}
