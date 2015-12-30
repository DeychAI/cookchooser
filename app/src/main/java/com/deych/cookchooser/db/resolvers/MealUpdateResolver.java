package com.deych.cookchooser.db.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealStorIOSQLitePutResolver;
import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

/**
 * Created by deigo on 30.12.2015.
 */
public class MealUpdateResolver extends DefaultPutResolver<Meal> {

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
        if (TextUtils.isEmpty(object.getClientId())) {
            return UpdateQuery.builder()
                    .table("meals")
                    .where("_id = ?")
                    .whereArgs(object.getId())
                    .build();
        }

        return UpdateQuery.builder()
                .table(MealTable.TABLE)
                .where(MealTable.CLIENT_ID + " = ?")
                .whereArgs(object.getClientId())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Meal object) {
        ContentValues contentValues = new ContentValues(5);

        contentValues.put("group_id", object.getGroup());
        contentValues.put("name", object.getName());
        contentValues.put("cat_id", object.getCategoryId());
        contentValues.put("_id", object.getId());
        contentValues.put("client_id", object.getClientId());

        return contentValues;
    }
}
