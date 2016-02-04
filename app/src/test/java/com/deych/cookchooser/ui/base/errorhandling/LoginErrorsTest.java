package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.ui.base.errorhandling.login.LoginViewCaseHandler;
import com.deych.cookchooser.ui.login.LoginView;

import org.junit.Before;
import org.junit.Test;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by deigo on 26.01.2016.
 */
public class LoginErrorsTest extends BaseUiHandlingTest<LoginView> {

    @Before
    public void before() {
        errorHandler = new ErrorHandler<>(new LoginViewCaseHandler());
        view = mock(LoginView.class);
    }

    @Test
    public void shouldHandle401Code() {
        Response<TokenResponse> response = Response.error(401, ResponseBody
                .create(MediaType.parse("text"), "Unauthorized Access"));
        errorHandler.handle(new HttpException(response)).resolve(view);

        verify(view).showInvalidCredentialsError();
    }
}
