package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.ui.base.LfView;
import com.deych.cookchooser.ui.base.views.NetworkErrorView;

/**
 * Created by deigo on 19.12.2015.
 */
public interface RegisterView extends LfView, NetworkErrorView{
    void showEmailNotValidError();
    void showUserExistsError();
    void showPasswordBlankError();
    void showPasswordMustMatchError();
    void registerSuccessful();
}
