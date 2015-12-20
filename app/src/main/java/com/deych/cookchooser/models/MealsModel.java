package com.deych.cookchooser.models;

import com.deych.cookchooser.api.entities.MealVo;
import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.User;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

import javax.inject.Inject;

import retrofit.Retrofit;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsModel {

    private User mUser;
    private MealsService mMealsService;
    private StorIOSQLite mStorIOSQLite;

    @Inject
    public MealsModel(User user, MealsService mealsService, StorIOSQLite storIOSQLite) {
        mUser = user;
        mMealsService = mealsService;
        mStorIOSQLite = storIOSQLite;
    }

    public Observable<List<MealVo>> list(long category_id) {
        return mMealsService.list(category_id);
    }

}
