package com.deych.cookchooser.db.resolvers;

import android.support.annotation.NonNull;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by deigo on 18.01.2016.
 */
public class MealDeleteResolver extends DefaultDeleteResolver<Meal> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull Meal meal) {
        return DeleteQuery.builder()
                .table(MealTable.TABLE)
                .where(MealTable.UUID + " = ?")
                .whereArgs(meal.getUuid())
                .build();
    }
}
