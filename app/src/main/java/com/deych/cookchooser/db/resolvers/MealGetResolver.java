package com.deych.cookchooser.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by deigo on 18.01.2016.
 */
public class MealGetResolver extends DefaultGetResolver<Meal> {
    @NonNull
    @Override
    public Meal mapFromCursor(@NonNull Cursor cursor) {
        Meal meal = new Meal();

        meal.setDeleted(cursor.getInt(cursor.getColumnIndex(MealTable.DELETED)) == 1);
        meal.setColor(MealColor.fromStringColor(cursor.getString(cursor.getColumnIndex(MealTable.COLOR))));
        meal.setGroup(cursor.getString(cursor.getColumnIndex(MealTable.GROUP_ID)));
        meal.setName(cursor.getString(cursor.getColumnIndex(MealTable.NAME)));
        meal.setImage(cursor.getString(cursor.getColumnIndex(MealTable.IMAGE)));
        meal.setDescription(cursor.getString(cursor.getColumnIndex(MealTable.DESCRIPTION)));
        meal.setCategoryId(cursor.getLong(cursor.getColumnIndex(MealTable.CATEGORY_ID)));
        meal.setUuid(cursor.getString(cursor.getColumnIndex(MealTable.UUID)));
        meal.setRevision(cursor.getLong(cursor.getColumnIndex(MealTable.REVISION)));
        meal.setChanged(cursor.getInt(cursor.getColumnIndex(MealTable.CHANGED)) == 1);

        return meal;

    }
}
