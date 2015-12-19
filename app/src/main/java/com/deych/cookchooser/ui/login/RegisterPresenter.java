package com.deych.cookchooser.ui.login;

import android.text.TextUtils;

import com.deych.cookchooser.api.ServiceFactory;
import com.deych.cookchooser.api.entities.*;
import com.deych.cookchooser.api.service.UserService;
import com.deych.cookchooser.ui.base.Presenter;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.HttpException;
import retrofit.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 19.12.2015.
 */
public class RegisterPresenter extends Presenter<RegisterView> {
    private ServiceFactory mServiceFactory;


    @Inject
    public RegisterPresenter(ServiceFactory aServiceFactory) {
        mServiceFactory = aServiceFactory;
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

        Subscription subscription = mServiceFactory.createService(UserService.class)
                .register(aUsername, aPassword, aName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(u -> {
                    if (view() != null) {
                        view().registerSuccessful();
                    }
                }, e -> {
                    HttpException error = (HttpException) e;
                    if (error.code() == 409) {
                        if (view() != null) {
                            view().showUserExistsError();
                        }
                        //TODO just a memo how to convert errors
//                        try {
//                            ResponseError responseError = (ResponseError) mRetrofit.responseConverter(ResponseError.class,
//                                    ResponseError.class.getAnnotations()).convert(error.response().errorBody());
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
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
}
