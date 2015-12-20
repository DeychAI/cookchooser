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
    long mId;

    @StorIOSQLiteColumn(name = UserTable.NAME)
    String mName;

    @StorIOSQLiteColumn(name = UserTable.LOGIN)
    String mUsername;

    @StorIOSQLiteColumn(name = UserTable.GROUP_ID)
    String mGroup;

    String mToken;

    public User() {
    }

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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }
}
