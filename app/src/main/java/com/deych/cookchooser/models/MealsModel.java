package com.deych.cookchooser.models;

import android.support.annotation.NonNull;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.tables.CategoryTable;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.user_scope.UserScope;
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
            if (dbMeals.contains(m)) {
                if (dbMeals.get(dbMeals.indexOf(m)).getRevision() != m.getRevision()) {
                    updateList.add(m);
                }
            } else {
                updateList.add(m);
            }
//            Meal found = null;
//            for (Meal d : dbMeals) {
//                if (m.getUuid().equals(d.getUuid())) {
//                    found = d;
//                    break;
//                }
//            }
//            if (found != null) {
//                if (found.getRevision() != m.getRevision()) {
//                    updateList.add(m);
//                }
//            } else {
//                updateList.add(m);
//            }
        }

        if (!updateList.isEmpty()) {
            storIOSQLite.put().objects(updateList)
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
            if (!netMeals.contains(d)) {
                forDelete.add(d);
            }
//            Meal found = null;
//            for (Meal m : netMeals) {
//                if (m.getUuid().equals(d.getUuid())) {
//                    found = d;
//                    break;
//                }
//            }
//            if (found == null) {
//                forDelete.add(d);
//            }
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
            serverAddMeal(send);
        }


        //Now we need to UPDATE items

        //Now we need to DELETE items

    }

    private void serverAddMeal(Meal send) {
        try {
            Response<Meal> response = mealsService.addMealCall(send).execute();
            if (response.isSuccess()) {
                storIOSQLite.put()
                        .object(response.body())
                        .prepare()
                        .executeAsBlocking();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serverUpdateMeal(Meal meal) {
        try {
            Response<Meal> response = mealsService.updateMealCall(meal.getUuid(), meal).execute();
            if (response.isSuccess()) {
                storIOSQLite.put()
                        .object(response.body())
                        .prepare()
                        .executeAsBlocking();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void processMealsResponse(List<Meal> netMeals) {

    }

    @NonNull
    public Observable<List<Meal>> getMealsFromDb(long category_id) {

        MealColor selectedColor = preferences.getSelectedColor();

        Query.CompleteBuilder query = Query.builder()
                .table(MealTable.TABLE)
                .orderBy(MealTable.NAME);
        if (!MealColor.None.equals(selectedColor)) {
            query.where(MealTable.CATEGORY_ID + " = ? and " + MealTable.COLOR + " = ?")
                    .whereArgs(category_id, selectedColor.color());

        } else {
            query.where(MealTable.CATEGORY_ID + " = ?").whereArgs(category_id);
        }

        return storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query.build())
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

    public Observable<Integer> getMealsCountForColor(MealColor color) {
        return storIOSQLite
                .get()
                .numberOfResults()
                .withQuery(Query.builder().table(MealTable.TABLE)
                        .where(MealTable.COLOR + " = ?")
                        .whereArgs(color.color())
                        .build())
                .prepare()
                .createObservable();
    }

    public Observable<Boolean> saveMeal(Meal meal) {
        if (meal.getUuid() == null) {
            meal.setUuid(UUID.randomUUID().toString());
            meal.setGroup(user.getGroup());
        } else {
            meal.setChanged(true);
        }

        return storIOSQLite
                .put()
                .object(meal)
                .prepare()
                .createObservable()
                .flatMap(putResult -> Observable.just(putResult.wasInserted() || putResult.wasUpdated()))
                .doOnNext(result -> {
                    if (result) {
                        processSaveMeal(meal);
                    }
                });
    }

    private void processSaveMeal(Meal meal) {
        if (!meal.isChanged()) {
            serverAddMeal(meal);
        } else {
            serverUpdateMeal(meal);
        }
    }

    public Observable<Meal> getMeal(String uuid) {
        return storIOSQLite.get()
                .object(Meal.class)
                .withQuery(Query.builder()
                        .table(MealTable.TABLE)
                        .where(MealTable.UUID + " = ?")
                        .whereArgs(uuid)
                        .build())
                .prepare()
                .createObservable()
                .take(1);
    }
}
