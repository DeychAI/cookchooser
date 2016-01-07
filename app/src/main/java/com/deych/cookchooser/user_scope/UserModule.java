package com.deych.cookchooser.user_scope;

import android.util.Base64;

import com.deych.cookchooser.db.entities.User;
import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

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
        return noAuthClient.newBuilder()
                .addInterceptor(authInterceptor)
                .build();
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
