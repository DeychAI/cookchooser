package com.deych.cookchooser.user_scope;

import android.util.Base64;

import com.deych.cookchooser.db.entities.User;
import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by deigo on 19.12.2015.
 */
@Module
public class UserModule {

    private User user;

    public UserModule(User user) {
        this.user = user;
    }

    @UserScope
    @Provides
    public User provideUser() {
        return user;
    }

    @Provides
    @UserScope
    public Interceptor provideAuthInterceptor(User user) {
        return chain -> {
            Request original = chain.request();
            String credentials = user.getToken() + ":";

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP))
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    @Provides
    @UserScope
    @Named("OkHttpWithAuth")
    public OkHttpClient provideOkHttpClient(OkHttpClient noAuthClient, Interceptor authInterceptor) {
        OkHttpClient client = noAuthClient.clone();
        client.interceptors().add(authInterceptor);
        return client;
    }

    @Provides
    @UserScope
    @Named("RetrofitWithAuth")
    public Retrofit provideRetrofit(Retrofit retrofit, Gson gson, @Named("OkHttpWithAuth") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(retrofit.baseUrl())
                .client(okHttpClient)
                .build();
    }
}
