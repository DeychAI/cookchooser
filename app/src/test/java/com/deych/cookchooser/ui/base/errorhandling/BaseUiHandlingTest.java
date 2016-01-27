package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.ui.base.views.NetworkErrorView;
import com.deych.cookchooser.ui.login.LoginView;
import com.pushtorefresh.storio.StorIOException;

import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.Mockito.verify;

/**
 * Created by deigo on 27.01.2016.
 */
public abstract class BaseUiHandlingTest<V extends NetworkErrorView> {
    protected ErrorHandler<V> errorHandler;
    protected V view;

    @Test
    public void shouldHandleIOException() {
        errorHandler.handle(new IOException()).resolve(view);
        verify(view).showNetworkError();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnUnhandledCode() {
        Response<TokenResponse> response = Response.error(400, ResponseBody
                .create(MediaType.parse("text"), "Bad Request Test"));
        errorHandler.handle(new HttpException(response)).resolve(view);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnUnknownError() {
        errorHandler.handle(new StorIOException("message")).resolve(view);
    }
}
