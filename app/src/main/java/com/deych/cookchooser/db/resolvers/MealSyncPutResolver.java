package com.deych.cookchooser.db.resolvers;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

/**
 * Created by deigo on 30.12.2015.
 */
public class MealSyncPutResolver extends DefaultPutResolver<Meal> {

    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull Meal object) {
        return InsertQuery.builder()
                .table(MealTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Meal object) {
        return UpdateQuery.builder()
                .table(MealTable.TABLE)
                .where(MealTable.UUID + " = ?")
                .whereArgs(object.getUuid())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Meal object) {
        ContentValues contentValues = new ContentValues(8);

        contentValues.put(MealTable.UUID, object.getUuid());
        contentValues.put(MealTable.GROUP_ID, object.getGroup());
        contentValues.put(MealTable.NAME, object.getName());
        contentValues.put(MealTable.CATEGORY_ID, object.getCategoryId());
        contentValues.put(MealTable.COLOR, object.getColor());
        contentValues.put(MealTable.REVISION, object.getRevision());
        contentValues.put(MealTable.CHANGED, object.isChanged());
        contentValues.put(MealTable.DELETED, object.isDeleted());

        return contentValues;
    }
}
