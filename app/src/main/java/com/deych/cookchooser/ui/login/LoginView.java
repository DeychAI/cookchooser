package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.ui.base.LfView;

/**
 * Created by deigo on 15.12.2015.
 */
public interface LoginView extends LfView{
    void showNetworkError();
    void showInvalidCredentialsError();
    void loginSuccessful(User user);
}
