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

    private final SharedPreferences mPreferences;

    @Inject
    public Preferences(Context aContext) {
        mPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(aContext);
    }

    public void saveUserData(long aId, String aToken) {
        mPreferences.edit()
                .putString(USER_TOKEN, aToken)
                .putLong(USER_ID, aId)
                .apply();
    }

    public void clearUserData() {
        mPreferences.edit().remove(USER_ID).remove(USER_TOKEN).apply();
    }

    public String getUserToken() {
        return mPreferences.getString(USER_TOKEN, "");
    }

    public long getUserId() {
        return mPreferences.getLong(USER_ID, 0);
    }

    public long getPresenterCacheIdAndIncrement() {
        return getLongAndIncrement(PRESENTER_CACHE_ID, 0);
    }

    public long getNewDbIdAndIncrement() {
        return getLongAndIncrement(NEW_DB_ID, Long.MIN_VALUE);
    }

    private long getLongAndIncrement(String key, long defaultValue) {
        long result = mPreferences.getLong(key, defaultValue);
        mPreferences.edit().putLong(key, result + 1).apply();
        return result;
    }
}
