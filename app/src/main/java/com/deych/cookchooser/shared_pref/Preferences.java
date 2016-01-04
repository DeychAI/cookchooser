package com.deych.cookchooser.shared_pref;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by deigo on 19.12.2015.
 */
public class Preferences {

    private static final String USER_ID = "user.id";
    private static final String USER_TOKEN = "user.token";
    private static final String PRESENTER_CACHE_ID = "presenter.cache.id";
    private static final String NEW_DB_ID = "new.db.id";

    private final SharedPreferences preferences;

    @Inject
    public Preferences(Context context) {
        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
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

    public long getNewDbIdAndIncrement() {
        return getLongAndIncrement(NEW_DB_ID, Long.MIN_VALUE);
    }

    private long getLongAndIncrement(String key, long defaultValue) {
        long result = preferences.getLong(key, defaultValue);
        preferences.edit().putLong(key, result + 1).apply();
        return result;
    }
}
