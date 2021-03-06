package com.deych.cookchooser.db.tables;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealTable {
    public static final String TABLE = "meals";

    public static final String NAME = "name";
    public static final String CATEGORY_ID = "cat_id";
    public static final String GROUP_ID = "group_id";
    public static final String UUID = "uuid";
    public static final String COLOR = "color";
    public static final String REVISION = "revision";
    public static final String CHANGED = "changed";
    public static final String DELETED = "deleted";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";

    public static String getCreateTableQuery() {
        return "create table " + TABLE + "("
                + UUID + " text primary key, "
                + NAME + " text, "
                + CATEGORY_ID + " integer, "
                + GROUP_ID + " text, "
                + COLOR + " text, "
                + IMAGE + " text, "
                + DESCRIPTION + " text, "
                + REVISION + " integer, "
                + CHANGED + " integer, "
                + DELETED + " integer"
                + ")";
    }

    public static final Query QUERY_ALL = Query.builder().table(TABLE).build();

    private MealTable() {
    }

}
