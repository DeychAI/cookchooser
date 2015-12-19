package com.deych.cookchooser.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deych.cookchooser.db.tables.UserTable;

/**
 * Created by deigo on 19.12.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public DbHelper(Context aContext) {
        super(aContext, "cookchooser_db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
    }
}
