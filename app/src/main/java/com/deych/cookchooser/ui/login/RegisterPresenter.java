package com.deych.cookchooser.ui.login;

import android.text.TextUtils;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 19.12.2015.
 */
public class RegisterPresenter extends Presenter<RegisterView> {
    private UserModel mUserModel;
    private Observable<User> mUserObservable;


    @Inject
    public RegisterPresenter(UserModel userModel) {
        mUserModel = userModel;
    }

    public void doRegister(String aUsername, String aPassword, String aRepeatPassword, String aName) {
        if (!validateUsername(aUsername)) {
            if (view() != null) {
                view().showEmailNotValidError();
            }
            return;
        }

        if (!validatePasswordBlank(aPassword)) {
            if (view() != null) {
                view().showPasswordBlankError();
            }
            return;
        }

        if (!validatePasswordMatch(aPassword, aRepeatPassword)) {
            if (view() != null) {
                view().showPasswordMustMatchError();
            }
            return;
        }

        if (view() != null) {
            view().showLoading();
        }

        mUserObservable = mUserModel.register(aUsername, aPassword, aName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = mUserObservable
                .subscribe(u -> {
                    if (view() != null) {
                        view().registerSuccessful();
                    }
                }, e -> {
                    if (mUserModel.handleRegisterError(e) == UserModel.ERROR_USER_EXISTS) {
                        if (view() != null) {
                            view().showUserExistsError();
                        }
                    } else {
                        if (view() != null) {
                            view().showError();
                        }
                    }
                });
        addToSubscription(subscription);
    }


    private boolean validateUsername(String aUsername) {
        return !(TextUtils.isEmpty(aUsername) || !android.util.Patterns.EMAIL_ADDRESS.matcher(aUsername).matches());
    }

    private boolean validatePasswordBlank(String aPassword) {
        return !(TextUtils.isEmpty(aPassword));
    }

    private boolean validatePasswordMatch(String aPassword, String aRepeatPassword) {
        return aPassword.equals(aRepeatPassword);
    }

    public void checkStateAfterRestore() {
        if (mUserObservable == null && view() != null) {
            view().showForm();
        }
    }
}
