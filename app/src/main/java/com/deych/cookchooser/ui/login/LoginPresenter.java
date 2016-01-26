package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.util.RxSchedulerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * Created by deigo on 16.12.2015.
 */
public class LoginPresenter extends Presenter<LoginView> {

    private UserModel userModel;
    private RxSchedulerFactory rxSchedulerFactory;
    private Observable<User> userObservable;

    @Inject
    public LoginPresenter(UserModel userModel, RxSchedulerFactory rxSchedulerFactory) {
        this.userModel = userModel;
        this.rxSchedulerFactory = rxSchedulerFactory;
    }

    public void doLogin(String username, String password) {
        if (view() != null) {
            view().showLoading();
        }

        userObservable = userModel.login(username, password)
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread());

        Subscription subscription = userObservable
                .subscribe(user -> {
                    if (view() != null) {
                        view().loginSuccessful(user);
                    }
                }, t -> {
                    int error = userModel.handleLoginError(t);
                    if (error == UserModel.ERROR_INVALID_CREDENTIALS) {
                        if (view() != null) {
                            view().showInvalidCredentialsError();
                        }
                    } else if (error == UserModel.ERROR_NETWORK) {
                        if (view() != null) {
                            view().showNetworkError();
                        }
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
