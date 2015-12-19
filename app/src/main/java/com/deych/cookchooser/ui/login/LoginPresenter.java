package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.api.ServiceFactory;
import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.api.service.TokenService;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.login.LoginView;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 16.12.2015.
 */
public class LoginPresenter extends Presenter<LoginView> {


    private ServiceFactory mServiceFactory;
    private Preferences mPreferences;
    private StorIOSQLite mStorIOSQLite;

    @Inject
    public LoginPresenter(ServiceFactory aServiceFactory, Preferences aPreferences, StorIOSQLite aStorIOSQLite) {
        mServiceFactory = aServiceFactory;
        mPreferences = aPreferences;
        mStorIOSQLite = aStorIOSQLite;
    }

    public void doLogin(String aUsername, String aPassword) {
        if (view() != null) {
            view().showLoading();
        }

        Subscription subscription = mServiceFactory.createService(TokenService.class, aUsername, aPassword)
                .login()
                .doOnNext(response -> {
                    com.deych.cookchooser.api.entities.User responseUser = response.getUser();
                    mPreferences.saveUserData(responseUser.getId(), response.getToken());
                    User user = User.newUser(responseUser.getId(), responseUser.getUsername(),
                            responseUser.getName(), responseUser.getGroup());
                    user.setToken(response.getToken());
                    mStorIOSQLite
                            .put()
                            .object(user)
                            .prepare()
                            .executeAsBlocking();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (view() != null) {
                        view().loginSuccessful();
                    }
                }, t -> {
                    if (view() != null) {
                        view().showError();
                    }
                });
        addToSubscription(subscription);
    }
}
