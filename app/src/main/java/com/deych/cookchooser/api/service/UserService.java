package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.entities.User;

import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by deigo on 19.12.2015.
 */
public interface UserService {

    @POST("users")
    Observable<User> register(@Query("username") String aUsername,
                              @Query("password") String aPassword,
                              @Query("name") String aName);
}
