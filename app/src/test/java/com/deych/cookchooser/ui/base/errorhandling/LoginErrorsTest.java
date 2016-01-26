package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.ui.base.errorhandling.login.LoginViewCaseHandler;
import com.deych.cookchooser.ui.login.LoginView;
import com.pushtorefresh.storio.StorIOException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.Mockito.*;

/**
 * Created by deigo on 26.01.2016.
 */
public class LoginErrorsTest {

    private ErrorHandler<LoginView> errorHandler;
    private LoginView view;

    @Before
    public void before() {
        errorHandler = new ErrorHandler<>(new LoginViewCaseHandler());
        view = mock(LoginView.class);
    }

    @Test
    public void shouldHandleIOException() {
        errorHandler.handle(new IOException()).resolve(view);
        verify(view).showNetworkError();
    }

    @Test
    public void shouldHandle401Code() {
        Response<TokenResponse> response = Response.error(401, ResponseBody
                .create(MediaType.parse("text"), "Unauthorized Access"));
        errorHandler.handle(new HttpException(response)).resolve(view);

        verify(view).showInvalidCredentialsError();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnUnhandledCode() {
        Response<TokenResponse> response = Response.error(404, ResponseBody
                .create(MediaType.parse("text"), "Not found"));
        errorHandler.handle(new HttpException(response)).resolve(view);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnUnknownError() {
        errorHandler.handle(new StorIOException("message")).resolve(view);
    }

}
