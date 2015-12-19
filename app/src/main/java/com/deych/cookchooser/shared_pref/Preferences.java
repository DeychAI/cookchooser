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
}
