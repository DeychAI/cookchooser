package com.deych.cookchooser.db.tables;

import android.provider.BaseColumns;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealTable {
    public static final String TABLE = "meals";

    public static final String NAME = "name";
    public static final String CATEGORY_ID = "cat_id";
    public static final String GROUP_ID = "group_id";
    public static final String CLIENT_ID = "client_id";
    public static final String COLOR = "color";
    public static final String REVISION = "revision";
    public static final String CHANGED = "changed";
    public static final String DELETED = "deleted";

    public static String getCreateTableQuery() {
        return "create table " + TABLE + "("
                + BaseColumns._ID + " integer primary key, "
                + NAME + " text, "
                + CATEGORY_ID + " integer, "
                + GROUP_ID + " text, "
                + CLIENT_ID + " text, "
                + COLOR + " text, "
                + REVISION + " integer, "
                + CHANGED + " integer, "
                + DELETED + " integer"
                + ")";
    }

    public static final Query QUERY_ALL = Query.builder().table(TABLE).build();

    private MealTable() {
    }

}
