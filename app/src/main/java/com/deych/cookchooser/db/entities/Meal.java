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
    long id;

    @StorIOSQLiteColumn(name = MealTable.NAME)
    String name;

    @StorIOSQLiteColumn(name = MealTable.CATEGORY_ID)
    long categoryId;

    @StorIOSQLiteColumn(name = MealTable.GROUP_ID)
    String group;

    @StorIOSQLiteColumn(name = MealTable.CLIENT_ID)
    String clientId;

    @StorIOSQLiteColumn(name = MealTable.COLOR)
    String color;

    @StorIOSQLiteColumn(name = MealTable.REVISION)
    long revision;

    @StorIOSQLiteColumn(name = MealTable.CHANGED)
    boolean changed;

    @StorIOSQLiteColumn(name = MealTable.DELETED)
    boolean deleted;

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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getColor() {
        return color;
    }

    public Meal setColor(String color) {
        this.color = color;
        return this;
    }

    public long getRevision() {
        return revision;
    }

    public Meal setRevision(long revision) {
        this.revision = revision;
        return this;
    }

    public boolean isChanged() {
        return changed;
    }

    public Meal setChanged(boolean changed) {
        this.changed = changed;
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Meal setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

}
