package com.deych.cookchooser.sync;

import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.user_scope.UserScope;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by deigo on 19.01.2016.
 */
@UserScope
public class MealsSync {

    private MealsService mealsService;
    private StorIOSQLite storIOSQLite;

    @Inject
    public MealsSync(MealsService mealsService, StorIOSQLite storIOSQLite) {
        this.mealsService = mealsService;
        this.storIOSQLite = storIOSQLite;
    }

    public void save(Meal meal) {
        if (meal.isDeleted()) {
            delete(meal);
        } else if (meal.isChanged() && meal.getRevision() > 0) {
            update(meal);
        } else {
            add(meal);
        }
    }

    public void sync(List<Meal> netMeals, Long cat_id) {
        updateExisting(netMeals, cat_id);
        clearFromRemoved(netMeals, cat_id);
        sendNew(cat_id);
        sendUpdated(cat_id);
        sendDeleted(cat_id);
    }

    private void sendDeleted(Long cat_id) {
        Query query;
        //Now we need to DELETE items
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.DELETED + " = ?")
                    .whereArgs(1).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ? and "
                    + MealTable.DELETED + " = ?").whereArgs(cat_id, 1).build();
        }

        List<Meal> forSending = storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        for (Meal send : forSending) {
            delete(send);
        }

    }

    private void sendUpdated(Long cat_id) {
        Query query;
        //Now we need to UPDATE items
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CHANGED + " = ?")
                    .whereArgs(1).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ? and "
                    + MealTable.CHANGED + " = ?").whereArgs(cat_id, 1).build();
        }

        List<Meal> forSending = storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        for (Meal send : forSending) {
            update(send);
        }

    }

    private void sendNew(Long cat_id) {
        Query query;
        //Now we need to send NEW items to the server
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).where("revision = ?")
                    .whereArgs(0).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ? and revision = ?")
                    .whereArgs(cat_id, 0).build();
        }

        List<Meal> forSending = storIOSQLite.get()
                .listOfObjects(Meal.class)
                .withQuery(query)
                .prepare()
                .executeAsBlocking();

        for (Meal send : forSending) {
            add(send);
        }
    }

    private void clearFromRemoved(List<Meal> netMeals, Long cat_id) {
        Query query;
        //Now we need to delete items that we didn't receive from the server (someone else deleted them)
        if (cat_id == null) {
            query = Query.builder().table(MealTable.TABLE).where("revision > ?")
                    .whereArgs(0).build();
        } else {
            query = Query.builder().table(MealTable.TABLE).where(MealTable.CATEGORY_ID + " = ? and revision > ?")
                    .whereArgs(cat_id, 0).build();
        }

        List<Meal> dbMeals = storIOSQLite
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
        }

        storIOSQLite.delete().objects(forDelete).prepare().executeAsBlocking();
    }

    private void updateExisting(List<Meal> netMeals, Long cat_id) {
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
        }

        if (!updateList.isEmpty()) {
            storIOSQLite.put().objects(updateList)
                    .prepare().executeAsBlocking();
        }
    }

    private void add(Meal send) {
        try {
            Response<Meal> response = mealsService.addMealCall(send).execute();
            if (response.isSuccess()) {
                storeToDb(response.body());
            } //TODO do something in case of UUIDs are not unique
        } catch (IOException e) {
            /*NOP*/
        }
    }

    private void update(Meal meal) {
        try {
            Response<Meal> response = mealsService.updateMealCall(meal.getUuid(), meal).execute();
            if (response.isSuccess()) {
                storeToDb(response.body());
            } else {
                if (response.code() == 409) {
                    resolveConflict(meal);
                }
                //TODO process 403 and 404
            }
        } catch (IOException e) {
            /*NOP*/
        }
    }

    private void delete(Meal meal) {
        try {
            Response<Response<ResponseBody>> response = mealsService.deleteMealCall(meal.getUuid(), meal.getRevision())
                    .execute();
            if (response.isSuccess()) {
                storIOSQLite.delete()
                        .object(meal)
                        .prepare()
                        .executeAsBlocking();
            } else {
                if (response.code() == 409) {
                    resolveConflict(meal);
                }
                //TODO process 403 and 404
            }
        } catch (IOException e) {
            /*NOP*/
        }
    }

    private void resolveConflict(Meal meal) {
        mealsService.getMeal(meal.getUuid())
                .doOnNext(this::storeToDb)
                .subscribe(m -> {
                    //TODO notify that conflict solved
                }, t -> {
                    //NOP
                });
    }

    private void storeToDb(Meal meal) {
        storIOSQLite.put()
                .object(meal)
                .prepare()
                .executeAsBlocking();

    }
}
