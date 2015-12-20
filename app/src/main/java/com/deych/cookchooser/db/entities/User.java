package com.deych.cookchooser.db.entities;

import android.provider.BaseColumns;

import com.deych.cookchooser.api.entities.UserVo;
import com.deych.cookchooser.db.tables.UserTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by deigo on 19.12.2015.
 */
@StorIOSQLiteType(table = UserTable.TABLE)
public class User {
    @StorIOSQLiteColumn(name = BaseColumns._ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = UserTable.USER_ID)
    long userId;
    @StorIOSQLiteColumn(name = UserTable.NAME)
    String name;
    @StorIOSQLiteColumn(name = UserTable.LOGIN)
    String username;
    @StorIOSQLiteColumn(name = UserTable.GROUP)
    String group;

    String token;

    public User() {
    }

    public static User newUser(long userId, String username, String name, String group) {
        User user = new User();
        user.userId = userId;
        user.username = username;
        user.name = name;
        user.group = group;
        return user;
    }

    public static User newUser(UserVo userVo) {
        return newUser(userVo.getId(), userVo.getUsername(), userVo.getName(), userVo.getGroup());
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
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
}
