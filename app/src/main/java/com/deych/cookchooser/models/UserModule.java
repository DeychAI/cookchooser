package com.deych.cookchooser.models;

import android.content.Context;
import android.util.Base64;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.User;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by deigo on 19.12.2015.
 */
@Module
public class UserModule {

    private User mUser;

    public UserModule(User user) {
        mUser = user;
    }

    @UserScope
    @Provides
    public User provideUser() {
        return mUser;
    }

    @Provides
    @UserScope
    public MealsModel provideMealsModel(User user, OkHttpClient okHttpClient,
                                        HttpLoggingInterceptor loggingInterceptor,
                                        MealsService mealsService,
                                        StorIOSQLite storIOSQLite,
                                        Context context) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String credentials = user.getToken() + ":";

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP))
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        okHttpClient.interceptors().clear();
        okHttpClient.interceptors().add(loggingInterceptor);
        okHttpClient.interceptors().add(interceptor);

        return new MealsModel(user, mealsService, storIOSQLite, context);
    }
}
