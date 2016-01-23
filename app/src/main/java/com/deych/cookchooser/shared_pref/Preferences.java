package com.deych.cookchooser.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.deych.cookchooser.db.entities.MealColor;

import javax.inject.Inject;

/**
 * Created by deigo on 19.12.2015.
 */
public class Preferences {

    private static final String USER_ID = "user.id";
    private static final String USER_TOKEN = "user.token";
    private static final String PRESENTER_CACHE_ID = "presenter.cache.id";
    private static final String SELECTED_COLOR = "selected.color";

    private final SharedPreferences preferences;

    @Inject
    public Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUserData(long id, String token) {
        preferences.edit()
                .putString(USER_TOKEN, token)
                .putLong(USER_ID, id)
                .apply();
    }

    public void clearUserData() {
        preferences.edit().remove(USER_ID).remove(USER_TOKEN).apply();
    }

    public String getUserToken() {
        return preferences.getString(USER_TOKEN, "");
    }

    public long getUserId() {
        return preferences.getLong(USER_ID, 0);
    }

    public long getPresenterCacheIdAndIncrement() {
        return getLongAndIncrement(PRESENTER_CACHE_ID, 0);
    }

    public void clearSelectedColor() {
        preferences.edit().remove(SELECTED_COLOR).apply();
    }

    public void saveSelectedColor(MealColor color) {
        preferences.edit().putString(SELECTED_COLOR, color.color()).apply();
    }

    public MealColor getSelectedColor() {
        return MealColor.fromStringColor(preferences.getString(SELECTED_COLOR, MealColor.None.color()));
    }

    private long getLongAndIncrement(String key, long defaultValue) {
        long result = preferences.getLong(key, defaultValue);
        preferences.edit().putLong(key, result + 1).apply();
        return result;
    }
}
