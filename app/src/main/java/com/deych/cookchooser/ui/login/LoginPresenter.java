package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 16.12.2015.
 */
public class LoginPresenter extends Presenter<LoginView> {

    private UserModel mUserModel;

    @Inject
    public LoginPresenter(UserModel userModel) {
        mUserModel = userModel;
    }

    public void doLogin(String username, String password) {
        if (view() != null) {
            view().showLoading();
        }

        Subscription subscription = mUserModel.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view() != null) {
                        view().loginSuccessful(user);
                    }
                }, t -> {
                    if (view() != null) {
                        view().showError();
                    }
                });
        addToSubscription(subscription);
    }
}
