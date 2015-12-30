package com.deych.cookchooser.ui;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

/**
 * Created by deigo on 30.12.2015.
 */
public class MainActivityPresenter extends Presenter<MainActivityView>{

    private User mUser;
    private UserModel mUserModel;

    @Inject
    public MainActivityPresenter(User user, UserModel userModel) {
        mUser = user;
        mUserModel = userModel;
    }

    @Override
    public void bindView(MainActivityView aView) {
        super.bindView(aView);
        if (view() != null) {
            view().bindUserData(mUser.getUsername(), mUser.getName());
        }
    }

    public void logout() {
        //TODO clear all data
        mUserModel.logout();
        if (view() != null) {
            view().showLoginScreen();
        }
    }

}
