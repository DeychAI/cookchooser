package com.deych.cookchooser.ui.base.config.impl;

import com.deych.cookchooser.ui.base.config.UiConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deigo on 04.02.2016.
 */
public class CompositeConfig implements UiConfig {

    private List<UiConfig> configs = new ArrayList<>();

    public CompositeConfig add(UiConfig config) {
        configs.add(config);
        return this;
    }

    @Override
    public void apply() {
        for (UiConfig config : configs) {
            config.apply();
        }
    }

    @Override
    public void release() {
        for (UiConfig config : configs) {
            config.release();
        }
    }
}
