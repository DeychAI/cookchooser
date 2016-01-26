package com.deych.cookchooser.ui.login;

import android.text.TextUtils;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.errorhandling.ErrorHandler;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 19.12.2015.
 */
public class RegisterPresenter extends Presenter<RegisterView> {

    private UserModel userModel;
    private Observable<User> userObservable;
    private ErrorHandler<RegisterView> errorHandler;

    @Inject
    public RegisterPresenter(UserModel userModel, ErrorHandler<RegisterView> errorHandler) {
        this.userModel = userModel;
        this.errorHandler = errorHandler;
    }

    public void doRegister(String username, String password, String repeatPassword, String name) {
        if (!validateUsername(username)) {
            if (view() != null) {
                view().showEmailNotValidError();
            }
            return;
        }

        if (!validatePasswordBlank(password)) {
            if (view() != null) {
                view().showPasswordBlankError();
            }
            return;
        }

        if (!validatePasswordMatch(password, repeatPassword)) {
            if (view() != null) {
                view().showPasswordMustMatchError();
            }
            return;
        }

        if (view() != null) {
            view().showLoading();
        }

        userObservable = userModel.register(username, password, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = userObservable
                .subscribe(u -> {
                    if (view() != null) {
                        view().registerSuccessful();
                    }
                }, e -> {
                    if (view() != null) {
                        errorHandler.handle(e).resolve(view());
                    }
                });
        addToSubscription(subscription);
    }


    private boolean validateUsername(String username) {
        return !(TextUtils.isEmpty(username) || !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches());
    }

    private boolean validatePasswordBlank(String password) {
        return !(TextUtils.isEmpty(password));
    }

    private boolean validatePasswordMatch(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    public void checkStateAfterRestore() {
        if (userObservable == null && view() != null) {
            view().showForm();
        }
    }
}
