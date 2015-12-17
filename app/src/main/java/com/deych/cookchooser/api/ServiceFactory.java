package com.deych.cookchooser.api;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Retrofit;

/**
 * Created by deigo on 13.12.2015.
 */
@Singleton
public class ServiceFactory {
    private final OkHttpClient mOkHttpClient;
    private final Retrofit mRetrofit;

    @Inject
    public ServiceFactory(OkHttpClient aOkHttpClient, Retrofit aRetrofit) {
        mOkHttpClient = aOkHttpClient;
        mRetrofit = aRetrofit;
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            final String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            mOkHttpClient.interceptors().clear();
            mOkHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
//                            .header("Accept", "applicaton/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        return mRetrofit.create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            mOkHttpClient.interceptors().clear();
            mOkHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        return mRetrofit.create(serviceClass);
    }
}
