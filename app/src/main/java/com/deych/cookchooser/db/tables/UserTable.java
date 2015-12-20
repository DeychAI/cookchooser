package com.deych.cookchooser.db.tables;

import android.provider.BaseColumns;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by deigo on 19.12.2015.
 */
public class UserTable {

    public static final String TABLE = "users";

    public static final String LOGIN = "username";
    public static final String NAME = "name";
    public static final String GROUP_ID = "group_id";

    public static String getCreateTableQuery() {
        return "create table " + TABLE + "("
                + BaseColumns._ID + " integer primary key, "
                + LOGIN + " text, "
                + NAME + " text, "
                + GROUP_ID + " text)";
    }

    public static Query get(long user_id) {
        return Query.builder()
                .table(TABLE)
                .where(BaseColumns._ID + " = ?")
                .whereArgs(user_id)
                .build();
    }

    private UserTable() {
    }
}
