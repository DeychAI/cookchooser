package com.deych.cookchooser.db.tables;

import android.provider.BaseColumns;

/**
 * Created by deigo on 19.12.2015.
 */
public class UserTable {

    public static final String TABLE = "users";

    public static final String USER_ID = "user_id";
    public static final String LOGIN = "username";
    public static final String NAME = "name";
    public static final String GROUP = "group_id";

    public static String getCreateTableQuery() {
        return "create table " + TABLE + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + USER_ID + " integer, "
                + LOGIN + " text, "
                + NAME + " text, "
                + GROUP + " text)";
    }

    private UserTable() {
    }
}
