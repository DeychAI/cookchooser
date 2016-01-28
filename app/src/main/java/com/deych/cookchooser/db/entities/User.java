package com.deych.cookchooser.db.entities;

import android.provider.BaseColumns;

import com.deych.cookchooser.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by deigo on 19.12.2015.
 */
@StorIOSQLiteType(table = UserTable.TABLE)
public class User {
    @StorIOSQLiteColumn(name = BaseColumns._ID, key = true)
    long id;

    @StorIOSQLiteColumn(name = UserTable.NAME)
    String name;

    @StorIOSQLiteColumn(name = UserTable.LOGIN)
    String username;

    @StorIOSQLiteColumn(name = UserTable.GROUP_ID)
    String group;

    @StorIOSQLiteColumn(name = UserTable.IMAGE)
    String image;

    String token;

    public User() {
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
