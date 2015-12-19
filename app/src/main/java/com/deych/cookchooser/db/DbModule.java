package com.deych.cookchooser.db;

import android.content.Context;

import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.entities.UserStorIOSQLiteDeleteResolver;
import com.deych.cookchooser.db.entities.UserStorIOSQLiteGetResolver;
import com.deych.cookchooser.db.entities.UserStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by deigo on 19.12.2015.
 */
@Module
public class DbModule {

    @Singleton
    @Provides
    public DbHelper provideDbHelper(Context aContext) {
        return new DbHelper(aContext);
    }

    @Provides
    @Singleton
    public StorIOSQLite provideStorIOSQLite(DbHelper aDbHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(aDbHelper)
                .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                        .putResolver(new UserStorIOSQLitePutResolver())
                        .getResolver(new UserStorIOSQLiteGetResolver())
                        .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                        .build())
                .build();
    }
}
