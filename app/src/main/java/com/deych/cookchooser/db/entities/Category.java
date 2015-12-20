package com.deych.cookchooser.db.entities;

import android.provider.BaseColumns;

import com.deych.cookchooser.db.tables.CategoryTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by deigo on 20.12.2015.
 */
@StorIOSQLiteType(table = CategoryTable.TABLE)
public class Category {
    @StorIOSQLiteColumn(name = BaseColumns._ID, key = true)
    long mId;

    @StorIOSQLiteColumn(name = CategoryTable.NAME)
    String mName;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
