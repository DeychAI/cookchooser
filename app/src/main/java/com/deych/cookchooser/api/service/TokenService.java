package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.response.TokenResponse;

import retrofit.Call;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by deigo on 13.12.2015.
 */
public interface TokenService {

    @GET("token")
    Observable<TokenResponse> login();
}
