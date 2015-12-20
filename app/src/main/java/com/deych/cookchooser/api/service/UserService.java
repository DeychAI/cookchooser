package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.response.TokenResponse;
import com.deych.cookchooser.db.entities.User;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by deigo on 19.12.2015.
 */
public interface UserService {

    @GET("token")
    Observable<TokenResponse> login(@Header("Authorization") String authorization);

    @POST("users")
    Observable<User> register(@Query("username") String aUsername,
                              @Query("password") String aPassword,
                              @Query("name") String aName);
}
