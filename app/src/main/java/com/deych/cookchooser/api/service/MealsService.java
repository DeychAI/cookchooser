package com.deych.cookchooser.api.service;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
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

    @PUT("meals/{uuid}")
    Call<Meal> updateMealCall(@Path("uuid") String uuid, @Body Meal meal);

    @DELETE("meals/{uuid}")
    Call<Response<ResponseBody>> deleteMealCall(@Path("uuid") String uuid, @Query("revision") long revision);

    @GET("meals/{uuid}")
    Call<Meal> getMealCall(@Path("uuid") String uuid);

    @GET("meals/{uuid}")
    Observable<Meal> getMeal(@Path("uuid") String uuid);
}
