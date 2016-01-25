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
import com.deych.cookchooser.sync.MealsSync;
import com.deych.cookchooser.user_scope.UserScope;
import com.deych.cookchooser.util.RetryWithDelayIf;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    private MealsSync mealsSync;

    @Inject
    public MealsModel(User user, MealsService mealsService, StorIOSQLite storIOSQLite, Preferences preferences,
                      MealsSync mealsSync) {
        this.user = user;
        this.mealsService = mealsService;
        this.storIOSQLite = storIOSQLite;
        this.preferences = preferences;
        this.mealsSync = mealsSync;
    }

    public Observable<List<Meal>> loadAllMeals() {
        return mealsService.getAllMeals()
                .doOnNext((netMeals) -> mealsSync.sync(netMeals, null))
                .retryWhen(new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    @NonNull
    public Observable<List<Meal>> getMealsFromDb(long category_id) {

        MealColor selectedColor = preferences.getSelectedColor();

        Query.CompleteBuilder query = Query.builder()
                .table(MealTable.TABLE)
                .orderBy(MealTable.NAME);
        if (!MealColor.None.equals(selectedColor)) {
            query.where(
                    MealTable.CATEGORY_ID + " = ? and " + MealTable.COLOR + " = ? and " + MealTable.DELETED + " = ?")
                    .whereArgs(category_id, selectedColor.color(), 0);

        } else {
            query.where(MealTable.CATEGORY_ID + " = ? and " + MealTable.DELETED + " = ?")
                    .whereArgs(category_id, 0);
        }

        return storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query.build())
                .prepare()
                .asRxObservable();
    }

    public Observable<List<Meal>> getMealsFromNet(long category_id) {
        return mealsService.getMealsForCat(category_id)
                .doOnNext((netMeals) -> mealsSync.sync(netMeals, category_id))
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
                .asRxObservable()
                .take(1);
    }

    public Observable<Integer> getMealsCountForColor(MealColor color) {
        return storIOSQLite
                .get()
                .numberOfResults()
                .withQuery(Query.builder().table(MealTable.TABLE)
                        .where(MealTable.COLOR + " = ? and " + MealTable.DELETED + " = ?")
                        .whereArgs(color.color(), 0)
                        .build())
                .prepare()
                .asRxObservable();
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
                .asRxObservable()
                .flatMap(putResult -> Observable.just(putResult.wasInserted() || putResult.wasUpdated()))
                .doOnCompleted(() -> mealsSync.save(meal));
    }


    public Observable<Boolean> deleteMeal(Meal meal) {
        if (meal.getRevision() == 0) {
            return storIOSQLite
                    .delete()
                    .object(meal)
                    .prepare()
                    .asRxObservable()
                    .flatMap(deleteResult -> Observable.just(deleteResult.numberOfRowsDeleted() > 0));
        }

        meal.setDeleted(true);
        return storIOSQLite
                .put()
                .object(meal)
                .prepare()
                .asRxObservable()
                .flatMap(putResult -> Observable.just(putResult.wasInserted() || putResult.wasUpdated()))
                .doOnCompleted(() -> mealsSync.save(meal));
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
                .asRxObservable()
                .take(1);
    }
}
