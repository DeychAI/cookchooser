package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.entities.Invite;
import com.deych.cookchooser.db.entities.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by deigo on 22.01.2016.
 */
public interface InvitesService {

    @GET("invites")
    Observable<List<Invite>> getInvites();

    @POST("invites")
    Observable<Invite> invite(@Query("to") String username);

    @DELETE("invites")
    Observable<Response<ResponseBody>> clear();

    @POST("invites/{id}")
    Observable<User> accept(@Path("id") long id);

    @DELETE("invites/{id}")
    Observable<Response<ResponseBody>> remove(@Path("id") long id);
}
