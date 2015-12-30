package com.deych.cookchooser.db.entities;

import android.provider.BaseColumns;

import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by deigo on 20.12.2015.
 */
@StorIOSQLiteType(table = MealTable.TABLE)
public class Meal {

    @StorIOSQLiteColumn(name = BaseColumns._ID, key = true)
    long mId;

    @StorIOSQLiteColumn(name = MealTable.NAME)
    String mName;

    @StorIOSQLiteColumn(name = MealTable.CATEGORY_ID)
    long mCategoryId;

    @StorIOSQLiteColumn(name = MealTable.GROUP_ID)
    String mGroup;

    @StorIOSQLiteColumn(name = MealTable.CLIENT_ID)
    String mClientId;

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

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }
}