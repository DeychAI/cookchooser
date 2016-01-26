package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.ui.login.RegisterView;
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
public class RegisterErrorsTest {

    private ErrorHandler<RegisterView> errorHandler;
    private RegisterView view;

    @Before
    public void before() {
        errorHandler = ErrorHandler.forRegister();
        view = mock(RegisterView.class);
    }

    @Test
    public void shouldHandleIOException() {
        errorHandler.handle(new IOException()).resolve(view);
        verify(view).showNetworkError();
    }

    @Test
    public void shouldHandle409Code() {
        Response<TokenResponse> response = Response.error(409, ResponseBody
                .create(MediaType.parse("text"), "Test"));
        errorHandler.handle(new HttpException(response)).resolve(view);

        verify(view).showUserExistsError();
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
