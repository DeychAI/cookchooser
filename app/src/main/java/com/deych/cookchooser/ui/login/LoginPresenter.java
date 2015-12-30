package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.UIScope;
import com.deych.cookchooser.ui.base.Presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 16.12.2015.
 */
@UIScope
public class LoginPresenter extends Presenter<LoginView> {

    private UserModel mUserModel;
    private Observable<User> mUserObservable;

    @Inject
    public LoginPresenter(UserModel userModel) {
        mUserModel = userModel;
    }

    public void doLogin(String username, String password) {
        if (view() != null) {
            view().showLoading();
        }

        mUserObservable = mUserModel.login(username, password)
                .delaySubscription(7, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = mUserObservable
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
        if (mUserObservable == null && view() != null) {
            view().showForm();
        }
    }
}
