package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.db.entities.User;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by deigo on 19.12.2015.
 */
public interface UserService {

    @GET("token")
    Observable<TokenResponse> login(@Header("Authorization") String authorization);

    @POST("users")
    Observable<User> register(@Query("username") String username,
                              @Query("password") String password,
                              @Query("name") String name);
}
