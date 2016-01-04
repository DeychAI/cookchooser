package com.deych.cookchooser.ui;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

/**
 * Created by deigo on 30.12.2015.
 */
public class MainActivityPresenter extends Presenter<MainActivityView>{

    private User user;
    private UserModel userModel;

    @Inject
    public MainActivityPresenter(User user, UserModel userModel) {
        this.user = user;
        this.userModel = userModel;
    }

    @Override
    public void bindView(MainActivityView view) {
        super.bindView(view);
        if (view() != null) {
            view().bindUserData(user.getUsername(), user.getName());
        }
    }

    public void logout() {
        //TODO clear all data
        userModel.logout();
        if (view() != null) {
            view().showLoginScreen();
        }
    }

}
