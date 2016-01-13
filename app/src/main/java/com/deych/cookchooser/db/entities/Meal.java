package com.deych.cookchooser.db.entities;

import com.deych.cookchooser.db.tables.MealTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by deigo on 20.12.2015.
 */
@StorIOSQLiteType(table = MealTable.TABLE)
public class Meal {

    @StorIOSQLiteColumn(name = MealTable.UUID, key = true)
    String uuid;

    @StorIOSQLiteColumn(name = MealTable.NAME)
    String name;

    @StorIOSQLiteColumn(name = MealTable.CATEGORY_ID)
    long categoryId;

    @StorIOSQLiteColumn(name = MealTable.GROUP_ID)
    String group;

    @StorIOSQLiteColumn(name = MealTable.COLOR)
    String color;

    @StorIOSQLiteColumn(name = MealTable.REVISION)
    long revision;

    @StorIOSQLiteColumn(name = MealTable.CHANGED)
    boolean changed;

    @StorIOSQLiteColumn(name = MealTable.DELETED)
    boolean deleted;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String clientId) {
        this.uuid = uuid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
