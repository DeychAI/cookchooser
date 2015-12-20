package com.deych.cookchooser.db.tables;

import android.provider.BaseColumns;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by deigo on 20.12.2015.
 */
public class CategoryTable {

    public static final String TABLE = "categories";

    public static final String NAME = "name";

    public static String getCreateTableQuery() {
        return "create table " + TABLE + "("
                + BaseColumns._ID + " integer primary key, "
                + NAME + " text)";
    }

    public static final Query QUERY_ALL = Query.builder().table(TABLE).build();

    private CategoryTable() {
    }
}
