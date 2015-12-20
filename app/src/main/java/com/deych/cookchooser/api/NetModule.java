package com.deych.cookchooser.api;

import com.deych.cookchooser.BuildConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import timber.log.Timber;

import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.BODY;
import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by deigo on 13.12.2015.
 */
@Module
public class NetModule {
    String mBaseUrl;

    public NetModule(String aBaseUrl) {
        mBaseUrl = aBaseUrl;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setFieldNamingStrategy(f -> {
                    String name = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(f);
                    name = name.substring(2, name.length()).toLowerCase();
                    return name;
                }).create();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setWriteTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.interceptors().add(httpLoggingInterceptor);
        return client;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}
