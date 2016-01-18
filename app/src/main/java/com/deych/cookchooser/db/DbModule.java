package com.deych.cookchooser.db;

import android.content.Context;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.CategoryStorIOSQLiteDeleteResolver;
import com.deych.cookchooser.db.entities.CategoryStorIOSQLiteGetResolver;
import com.deych.cookchooser.db.entities.CategoryStorIOSQLitePutResolver;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.entities.UserStorIOSQLiteDeleteResolver;
import com.deych.cookchooser.db.entities.UserStorIOSQLiteGetResolver;
import com.deych.cookchooser.db.entities.UserStorIOSQLitePutResolver;
import com.deych.cookchooser.db.resolvers.MealDeleteResolver;
import com.deych.cookchooser.db.resolvers.MealGetResolver;
import com.deych.cookchooser.db.resolvers.MealPutResolver;
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
    public DbHelper provideDbHelper(Context context) {
        return new DbHelper(context);
    }

    @Provides
    @Singleton
    public StorIOSQLite provideStorIOSQLite(DbHelper dbHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(dbHelper)
                .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                        .putResolver(new UserStorIOSQLitePutResolver())
                        .getResolver(new UserStorIOSQLiteGetResolver())
                        .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Category.class, SQLiteTypeMapping.<Category>builder()
                        .putResolver(new CategoryStorIOSQLitePutResolver())
                        .getResolver(new CategoryStorIOSQLiteGetResolver())
                        .deleteResolver(new CategoryStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Meal.class, SQLiteTypeMapping.<Meal>builder()
                        .putResolver(new MealPutResolver())
                        .getResolver(new MealGetResolver())
                        .deleteResolver(new MealDeleteResolver())
                        .build())
                .build();
    }
}
