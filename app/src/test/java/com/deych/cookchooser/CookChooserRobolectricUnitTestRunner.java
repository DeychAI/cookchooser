package com.deych.cookchooser;

import android.support.annotation.NonNull;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

/**
 * Created by deigo on 23.01.2016.
 */
public class CookChooserRobolectricUnitTestRunner extends RobolectricGradleTestRunner {

    private static final int SDK_EMULATE_LEVEL = 21;

    public CookChooserRobolectricUnitTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public Config getConfig(@NonNull Method method) {
        final Config defaultConfig = super.getConfig(method);
        return new Config.Implementation(
                new int[]{SDK_EMULATE_LEVEL},
                defaultConfig.manifest(),
                defaultConfig.qualifiers(),
                defaultConfig.packageName(),
                defaultConfig.resourceDir(),
                defaultConfig.assetDir(),
                defaultConfig.shadows(),
                UnitTestApp.class,
                defaultConfig.libraries(),
                defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
        );
    }

}
