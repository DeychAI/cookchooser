package com.deych.cookchooser.ui.login;

import com.deych.cookchooser.ui.base.LfView;

/**
 * Created by deigo on 19.12.2015.
 */
public interface RegisterView extends LfView{
    void showEmailNotValidError();
    void showUserExistsError();
    void showPasswordBlankError();
    void showPasswordMustMatchError();
    void showError();
    void registerSuccessful();
}
