package com.deych.cookchooser.models;

import android.support.annotation.NonNull;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.resolvers.MealSyncPutResolver;
import com.deych.cookchooser.db.tables.CategoryTable;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.user_scope.UserScope;
import com.pushtorefresh.storio.StorIOException;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Response;
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
                .doOnNext((netMeals) -> processMealsResponse1(netMeals, null))
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    private void processMealsResponse1(List<Meal> netMeals, Long cat_id) {
        Query query;
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ?")
                    .whereArgs(cat_id).build();
        }

        List<Meal> dbMeals = storIOSQLite
                .get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        List<Meal> updateList = new ArrayList<>();

        for (Meal m : netMeals) {
            Meal found = null;
            for (Meal d : dbMeals) {
                if (m.getUuid().equals(d.getUuid())) {
                    found = d;
                    break;
                }
            }
            if (found != null) {
                if (found.getRevision() != m.getRevision()) {
                    updateList.add(m);
                }
            } else {
                updateList.add(m);
            }
        }

        if (!updateList.isEmpty()) {
            storIOSQLite.put().objects(updateList).withPutResolver(new MealSyncPutResolver())
                    .prepare().executeAsBlocking();
        }

        //Now we need to delete items that we didn't receive from the server (someone else deleted them)
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).where("revision > ?")
                    .whereArgs(0).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ? and revision > ?")
                    .whereArgs(cat_id, 0).build();
        }

        dbMeals = storIOSQLite
                .get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        List<Meal> forDelete = new ArrayList<>();

        for (Meal d : dbMeals) {
            Meal found = null;
            for (Meal m : netMeals) {
                if (m.getUuid().equals(d.getUuid())) {
                    found = d;
                    break;
                }
            }
            if (found == null) {
                forDelete.add(d);
            }
        }

        storIOSQLite.delete().objects(forDelete).prepare().executeAsBlocking();

        //Now we need to send NEW items to the server
        query = Query.builder().table(MealTable.TABLE).where("revision = ?")
                .whereArgs(0).build();

        List<Meal> forSending = storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        for (Meal send : forSending) {
            try {
                Response<Meal> response = mealsService.addMealCall(send).execute();
                if (response.isSuccess()) {
                    storIOSQLite.put()
                            .object(response.body())
                            .withPutResolver(new MealSyncPutResolver())
                            .prepare()
                            .executeAsBlocking();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //Now we need to UPDATE items

        //Now we need to DELETE items

    }

    private void processMealsResponse(List<Meal> netMeals) {
        storIOSQLite
                .put()
                .objects(netMeals)
                .withPutResolver(new MealSyncPutResolver())
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
                            .withPutResolver(new MealSyncPutResolver())
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
                .doOnNext((netMeals) -> processMealsResponse1(netMeals, category_id))
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    public Observable<List<Category>> reloadCategories() {
        Observable<List<Category>> db = getCategoriesFromDb();

        Observable<List<Category>> net = mealsService.getCategories()
                .doOnNext(list -> {
                    storIOSQLite.put().objects(list).prepare().executeAsBlocking();
                })
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));

        return Observable.concat(db, net);
    }

    @NonNull
    public Observable<List<Category>> getCategoriesFromDb() {
        return storIOSQLite.get()
                    .listOfObjects(Category.class)
                    .withQuery(CategoryTable.QUERY_ALL)
                    .prepare()
                    .createObservable()
                    .take(1);
    }

//    public Observable<Meal> addMeal(long category_id, String name) {
//        Meal meal = new Meal();
//        meal.setId(preferences.getNewDbIdAndIncrement());
//        meal.setCategoryId(category_id);
//        meal.setName(name);
//        meal.setClientId(UUID.randomUUID().toString());
//        meal.setColor("none");
//        meal.setGroup(user.getGroup());
//
//        return storIOSQLite
//                .put()
//                .object(meal)
//                .prepare()
//                .createObservable()
//                .flatMap(putResult -> {
//                    if (putResult.wasInserted()) {
//                        return mealsService.addMeal(meal);
//                    }
//                    throw new StorIOException("Was not inserted!");
//                })
//                .doOnNext(netMeal -> {
//                    storIOSQLite.put()
//                            .object(netMeal)
//                            .withPutResolver(new MealSyncPutResolver())
//                            .prepare()
//                            .executeAsBlocking();
//                });
//    }
}
