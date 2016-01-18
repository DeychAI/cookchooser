package com.deych.cookchooser.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deych.cookchooser.db.tables.CategoryTable;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.db.tables.UserTable;

/**
 * Created by deigo on 19.12.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 7;

    public DbHelper(Context context) {
        super(context, "cookchooser_db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(UserTable.getCreateTableQuery());
        db.execSQL(CategoryTable.getCreateTableQuery());
        db.execSQL(MealTable.getCreateTableQuery());
    }

    private void dropAllTables(SQLiteDatabase db) {
        dropTable(db, UserTable.TABLE);
        dropTable(db, CategoryTable.TABLE);
        dropTable(db, MealTable.TABLE);
    }

    private void dropTable(SQLiteDatabase db, String table) {
        db.execSQL("drop table if exists " + table);
    }
}
