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

    String color;

    long revision;

    boolean changed;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Meal meal = (Meal) o;

        if (id != meal.id) {
            return false;
        }
        if (categoryId != meal.categoryId) {
            return false;
        }
        if (name != null ? !name.equals(meal.name) : meal.name != null) {
            return false;
        }
        if (group != null ? !group.equals(meal.group) : meal.group != null) {
            return false;
        }
        return !(clientId != null ? !clientId.equals(meal.clientId) : meal.clientId != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        return result;
    }
}
