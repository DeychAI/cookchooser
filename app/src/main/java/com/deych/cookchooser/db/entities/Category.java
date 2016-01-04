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
    long id;

    @StorIOSQLiteColumn(name = CategoryTable.NAME)
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
