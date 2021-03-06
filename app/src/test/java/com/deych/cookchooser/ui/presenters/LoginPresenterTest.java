package com.deych.cookchooser.ui.presenters;

import com.deych.cookchooser.MockRxSchedulerFactory;
import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.errorhandling.ErrorHandler;
import com.deych.cookchooser.ui.base.errorhandling.login.LoginViewCaseHandler;
import com.deych.cookchooser.ui.login.LoginPresenter;
import com.deych.cookchooser.ui.login.LoginView;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

import static org.mockito.Mockito.*;

/**
 * Created by deigo on 23.01.2016.
 */
public class LoginPresenterTest {

    private UserModel userModel;
    private LoginView loginView;


    private LoginPresenter loginPresenter;

    @Before
    public void before() {
        userModel = mock(UserModel.class);
        loginView = mock(LoginView.class);

        loginPresenter = new LoginPresenter(userModel, new MockRxSchedulerFactory(),
                new ErrorHandler<>(new LoginViewCaseHandler()));
        loginPresenter.bindView(loginView);
    }

    @Test
    public void doLogin_shouldShowProgress() {
        when(userModel.login(anyString(), anyString())).thenReturn(Observable.just(new User()));

        loginPresenter.doLogin("test", "test");
        verify(loginView).showLoading();
    }

    @Test
    public void doLogin_shouldCallLoginSuccessful() {
        when(userModel.login(anyString(), anyString())).thenReturn(Observable.just(new User()));

        loginPresenter.doLogin("test", "test");
        verify(loginView).loginSuccessful(any());
    }

    @Test
    public void doLogin_shouldShowNetworkError() {
        when(userModel.login(anyString(), anyString())).thenReturn(Observable.error(new IOException()));

        loginPresenter.doLogin("test", "test");
        verify(loginView).showNetworkError();
    }

    @Test
    public void doLogin_shouldShowInvalidCredentialsError() {
        Response<TokenResponse> response = Response.error(401, ResponseBody.create(MediaType.parse("text"), "Unauthorized Access"));
        when(userModel.login(anyString(), anyString())).thenReturn(Observable.error(new HttpException(response)));

        loginPresenter.doLogin("test", "test");
        verify(loginView).showInvalidCredentialsError();
    }

    @Test
    public void checkStateAfterRestore_shouldShowForm() {
        loginPresenter.checkStateAfterRestore();
        verify(loginView).showForm();
    }
}
