package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 16.12.2015.
 */
public class LoginPresenter extends Presenter<LoginView> {

    private UserModel userModel;
    private Observable<User> userObservable;

    @Inject
    public LoginPresenter(UserModel userModel) {
        this.userModel = userModel;
    }

    public void doLogin(String username, String password) {
        if (view() != null) {
            view().showLoading();
        }

        userObservable = userModel.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = userObservable
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

    public void checkStateAfterRestore() {
        if (userObservable == null && view() != null) {
            view().showForm();
        }
    }
}
