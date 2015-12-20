package com.deych.cookchooser.api;

import com.deych.cookchooser.db.entities.User;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Retrofit;

/**
 * Created by deigo on 20.12.2015.
 */
public class ServiceTokenFactory {
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private Interceptor mInterceptor;

    @Inject
    public ServiceTokenFactory(Retrofit retrofit, OkHttpClient okHttpClient, User user) {
        mRetrofit = retrofit;
        mOkHttpClient = okHttpClient;

        mInterceptor = chain -> {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", user.getToken())
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    public <S> S createService(Class<S> serviceClass) {
        mOkHttpClient.interceptors().clear();
        mOkHttpClient.interceptors().add(mInterceptor);
        return mRetrofit.create(serviceClass);
    }

}
