package com.deych.cookchooser.api.service;

import com.deych.cookchooser.api.entities.MealVo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
public interface MealsService {

    @GET("meals")
    Observable<List<MealVo>> list(@Query("cat") long category_id);
}
