package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.ui.base.errorhandling.register.RegisterViewCaseHandler;
import com.deych.cookchooser.ui.login.RegisterView;

import org.junit.Before;
import org.junit.Test;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

import static org.mockito.Mockito.*;

/**
 * Created by deigo on 26.01.2016.
 */
public class RegisterErrorsTest extends BaseUiHandlingTest<RegisterView> {

    @Before
    public void before() {
        errorHandler = new ErrorHandler<>(new RegisterViewCaseHandler());
        view = mock(RegisterView.class);
    }

    @Test
    public void shouldHandle409Code() {
        Response<TokenResponse> response = Response.error(409, ResponseBody
                .create(MediaType.parse("text"), "Test"));
        errorHandler.handle(new HttpException(response)).resolve(view);

        verify(view).showUserExistsError();
    }
}
