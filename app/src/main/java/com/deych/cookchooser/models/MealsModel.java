package com.deych.cookchooser.models;

import android.content.Context;

import android.support.annotation.NonNull;
import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.tables.CategoryTable;
import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsModel {

    private User mUser;
    private MealsService mMealsService;
    private StorIOSQLite mStorIOSQLite;
    private Context mContext;

    @Inject
    public MealsModel(User user, MealsService mealsService, StorIOSQLite storIOSQLite, Context context) {
        mUser = user;
        mMealsService = mealsService;
        mStorIOSQLite = storIOSQLite;
        mContext = context;
    }

    public Observable<List<Meal>> getAllMealsFromNet() {
        return mMealsService.getAllMeals()
                .doOnNext(list -> {
                    mStorIOSQLite.put().objects(list).prepare().executeAsBlocking();
                })
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    @NonNull
    public Observable<List<Meal>> getMealsFromDb(final long category_id) {
        return mStorIOSQLite.get()
                    .listOfObjects(Meal.class)
                    .withQuery(Query.builder()
                            .table(MealTable.TABLE)
                            .where(MealTable.CATEGORY_ID + " = ?")
                            .whereArgs(category_id).build())
                    .prepare()
                    .createObservable();
    }

    public Observable<List<Meal>> getMealsFromNet(long category_id) {
        return mMealsService.getMealsForCat(category_id)
                .doOnNext(list -> {
                    mStorIOSQLite.put().objects(list).prepare().executeAsBlocking();
                })
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    public Observable<List<Category>> getCategories() {
        Observable<List<Category>> db = mStorIOSQLite.get()
                .listOfObjects(Category.class)
                .withQuery(CategoryTable.QUERY_ALL)
                .prepare()
                .createObservable()
                .take(1);

        Observable<List<Category>> net = mMealsService.getCategories()
                .doOnNext(list -> {
                    mStorIOSQLite.put().objects(list).prepare().executeAsBlocking();
                })
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));

        return Observable.concat(db, net);
    }
}
