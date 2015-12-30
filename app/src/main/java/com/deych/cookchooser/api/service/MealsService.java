package com.deych.cookchooser.api.service;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
public interface MealsService {

    @GET("meals")
    Observable<List<Meal>> getMealsForCat(@Query("cat") long category_id);

    @GET("meals")
    Observable<List<Meal>> getAllMeals();

    @GET("categories")
    Observable<List<Category>> getCategories();

    @POST("meals")
    Observable<Meal> addMeal(@Body Meal meal);

    @POST("meals")
    Call<Meal> addMealCall(@Body Meal meal);

}
