package com.deych.cookchooser.models;

import android.text.TextUtils;
import android.util.Base64;

import com.deych.cookchooser.api.service.UserService;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.tables.UserTable;
import com.deych.cookchooser.shared_pref.Preferences;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.HttpException;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
@Singleton
public class UserModel {

    private Preferences preferences;
    private StorIOSQLite storIOSQLite;
    private UserService userService;

    @Inject
    public UserModel(UserService userService, Preferences preferences, StorIOSQLite storIOSQLite) {
        this.userService = userService;
        this.preferences = preferences;
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<User> register(String username, String password, String name) {
        return userService.register(username, password, name);
    }
    public Observable<User> login(String username, String password) {
        final String credentials = username + ":" + password;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return userService.login(basic)
                .map(response -> {
                    User user = response.getUser();
                    user.setToken(response.getToken());
                    storIOSQLite
                            .put()
                            .object(user)
                            .prepare()
                            .executeAsBlocking();
                    return user;
                })
                .doOnNext(u -> preferences.saveUserData(u.getId(), u.getToken()));
    }

    public Observable<User> login() {
        if (preferences.getUserId() != 0 && !TextUtils.isEmpty(preferences.getUserToken())) {
            return storIOSQLite
                    .get()
                    .object(User.class)
                    .withQuery(UserTable.get(preferences.getUserId()))
                    .prepare()
                    .asRxObservable()
                    .take(1)
                    .map(user -> {
                        if (user == null) {
                            preferences.clearUserData();
                            throw new RuntimeException("User not found");
                        } else {
                            user.setToken(preferences.getUserToken());
                            return user;
                        }
                    });
        } else {
            return Observable.error(new RuntimeException("User not found"));
        }
    }

    public User loginAsBlocking() {
        if (preferences.getUserId() != 0 && !TextUtils.isEmpty(preferences.getUserToken())) {
            User user = storIOSQLite
                    .get()
                    .object(User.class)
                    .withQuery(UserTable.get(preferences.getUserId()))
                    .prepare()
                    .executeAsBlocking();
            if (user == null) {
                preferences.clearUserData();
                return null;
            } else {
                user.setToken(preferences.getUserToken());
                return user;
            }
        }
        return null;
    }

    public void logout() {
        preferences.clearUserData();
        preferences.clearSelectedColor();
    }


    public void colorSelected(MealColor color) {
        preferences.saveSelectedColor(color);
    }

    public MealColor getSelectedColor() {
        return preferences.getSelectedColor();
    }

}
