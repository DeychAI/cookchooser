package com.deych.cookchooser.models;

import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Base64;

import com.deych.cookchooser.api.service.UserService;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.tables.UserTable;
import com.deych.cookchooser.shared_pref.Preferences;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.HttpException;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
@Singleton
public class UserModel {
    public static final int ERROR_USER_EXISTS = 1;
    public static final int ERROR_OTHER = 2;

    private Preferences mPreferences;
    private StorIOSQLite mStorIOSQLite;
    private UserService mUserService;


    @Inject
    public UserModel(UserService userService, Preferences preferences, StorIOSQLite storIOSQLite) {
        mUserService = userService;
        mPreferences = preferences;
        mStorIOSQLite = storIOSQLite;
    }

    public Observable<User> register(String username, String password, String name) {
        return mUserService.register(username, password, name);
    }

    public int handleRegisterError(Throwable e) {
        if (!(e instanceof HttpException)) {
            return ERROR_OTHER;
        }
        HttpException error = (HttpException) e;
        if (error.code() == 409) {
            return ERROR_USER_EXISTS;
            //TODO just a memo how to convert errors
//                        try {
//                            ResponseError responseError = (ResponseError) mRetrofit.responseConverter(ResponseError.class,
//                                    ResponseError.class.getAnnotations()).convert(error.response().errorBody());
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
        }
        return ERROR_OTHER;
    }

    public Observable<User> login(String username, String password) {
        final String credentials = username + ":" + password;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return mUserService.login(basic)
                .map(response -> {
                    User user = response.getUser();
                    user.setToken(response.getToken());
                    mStorIOSQLite
                            .put()
                            .object(user)
                            .prepare()
                            .executeAsBlocking();
                    return user;
                })
                .doOnNext(u -> mPreferences.saveUserData(u.getId(), u.getToken()));
    }

    public Observable<User> login() {
        if (mPreferences.getUserId() != 0 && !TextUtils.isEmpty(mPreferences.getUserToken())) {
            return mStorIOSQLite
                    .get()
                    .listOfObjects(User.class)
                    .withQuery(UserTable.get(mPreferences.getUserId()))
                    .prepare()
                    .createObservable()
                    .take(1)
                    .map(list -> {
                        if (list.isEmpty()) {
                            mPreferences.clearUserData();
                            throw new RuntimeException("User not found");
                        } else {
                            User user = list.get(0);
                            user.setToken(mPreferences.getUserToken());
                            return user;
                        }
                    });
        } else {
            return Observable.error(new RuntimeException("User not found"));
        }
    }

    public void logout() {
        mPreferences.clearUserData();
    }
}
