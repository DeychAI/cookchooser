package com.deych.cookchooser.models;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.resolvers.MealUpdateResolver;
import com.deych.cookchooser.db.tables.CategoryTable;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.user_scope.UserScope;
import com.pushtorefresh.storio.StorIOException;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by deigo on 20.12.2015.
 */
@UserScope
public class MealsModel {

    private User user;
    private MealsService mealsService;
    private StorIOSQLite storIOSQLite;
    private Preferences preferences;

    @Inject
    public MealsModel(User user, MealsService mealsService, StorIOSQLite storIOSQLite, Preferences preferences) {
        this.user = user;
        this.mealsService = mealsService;
        this.storIOSQLite = storIOSQLite;
        this.preferences = preferences;
    }

    public Observable<List<Meal>> loadAllMeals() {
        return mealsService.getAllMeals()
                .doOnNext(this::processMealsResponse)
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    private void processMealsResponse(List<Meal> netMeals) {
        storIOSQLite
                .put()
                .objects(netMeals)
                .withPutResolver(new MealUpdateResolver())
                .prepare()
                .executeAsBlocking();

        List<Meal> dbMeals = storIOSQLite
                .get()
                .listOfObjects(Meal.class)
                .withQuery(Query.builder()
                        .table(MealTable.TABLE)
                        .where("_id > ?")
                        .whereArgs(0)
                        .build())
                .prepare()
                .executeAsBlocking();

        List<Meal> forDelete = new ArrayList<>();

        for (Meal db : dbMeals) {
            if (!netMeals.contains(db)) {
                forDelete.add(db);
            }
        }
        storIOSQLite.delete().objects(forDelete).prepare().executeAsBlocking();

        List<Meal> forSending = storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(Query.builder()
                        .table(MealTable.TABLE)
                        .where("_id < ?")
                        .whereArgs(0)
                        .build())
                .prepare()
                .executeAsBlocking();

        for (Meal forSend : forSending) {
            try {
                Response<Meal> response = mealsService.addMealCall(forSend).execute();
                if (response.isSuccess()) {
                    storIOSQLite.put()
                            .object(response.body())
                            .withPutResolver(new MealUpdateResolver())
                            .prepare()
                            .executeAsBlocking();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    public Observable<List<Meal>> getMealsFromDb(long category_id) {
        return storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(Query.builder()
                        .table(MealTable.TABLE)
                        .where(MealTable.CATEGORY_ID + " = ?")
                        .whereArgs(category_id)
                        .orderBy(MealTable.NAME)
                        .build())
                .prepare()
                .createObservable();
    }

    public Observable<List<Meal>> getMealsFromNet(long category_id) {
        return mealsService.getMealsForCat(category_id)
                .doOnNext(this::processMealsResponse)
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    public Observable<List<Category>> getCategories() {
        Observable<List<Category>> db = storIOSQLite.get()
                .listOfObjects(Category.class)
                .withQuery(CategoryTable.QUERY_ALL)
                .prepare()
                .createObservable()
                .take(1);

        Observable<List<Category>> net = mealsService.getCategories()
                .doOnNext(list -> {
                    storIOSQLite.put().objects(list).prepare().executeAsBlocking();
                })
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));

        return Observable.concat(db, net);
    }

    public Observable<Meal> addMeal(long category_id, String name) {
        Meal meal = new Meal();
        meal.setId(preferences.getNewDbIdAndIncrement());
        meal.setCategoryId(category_id);
        meal.setName(name);
        meal.setClientId(UUID.randomUUID().toString());
        meal.setGroup(user.getGroup());

        return storIOSQLite
                .put()
                .object(meal)
                .prepare()
                .createObservable()
                .flatMap(putResult -> {
                    if (putResult.wasInserted()) {
                        return mealsService.addMeal(meal);
                    }
                    throw new StorIOException("Was not inserted!");
                })
                .doOnNext(netMeal -> {
                    storIOSQLite.put()
                            .object(netMeal)
                            .withPutResolver(new MealUpdateResolver())
                            .prepare()
                            .executeAsBlocking();
                });
    }
}
