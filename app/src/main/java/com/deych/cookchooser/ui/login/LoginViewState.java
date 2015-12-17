package com.deych.cookchooser.ui.login;

import android.os.Bundle;

import com.deych.cookchooser.ui.base.ViewState;

/**
 * Created by deigo on 15.12.2015.
 */
public class LoginViewState implements ViewState<LoginView> {

    final int STATE_SHOW_LOGIN_FORM = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;

    int state = STATE_SHOW_LOGIN_FORM;

    public void setShowLoginForm() {
        state = STATE_SHOW_LOGIN_FORM;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    @Override
    public void apply(LoginView view) {
        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;

            case STATE_SHOW_ERROR:
                view.showError();
                break;

            case STATE_SHOW_LOGIN_FORM:
                view.showLoginForm();
                break;
        }
    }

    @Override
    public void saveInstanceState(Bundle out) {
        out.putInt("state", state);
    }

    @Override
    public ViewState<LoginView> restoreInstanceState(Bundle in) {
        if (in != null) {
            LoginViewState viewState = new LoginViewState();
            viewState.state = in.getInt("state");
            return viewState;
        }
        return null;
    }


}