package com.deych.cookchooser.ui.base.config.impl;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.deych.cookchooser.ui.base.config.TabLayoutUi;
import com.deych.cookchooser.ui.base.config.UiConfig;

/**
 * Created by deigo on 04.02.2016.
 */
public class TabLayoutConfig implements UiConfig {

    private TabLayoutUi ui;
    private boolean shouldShow;

    public TabLayoutConfig(Context context) {
        try {
            ui = (TabLayoutUi) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TabLayoutUi");
        }
    }

    public TabLayoutConfig show() {
        this.shouldShow = true;
        return this;
    }

    public TabLayoutConfig hide() {
        this.shouldShow = false;
        return this;
    }

    public TabLayout tabs() {
        return ui.tabs();
    }

    @Override
    public void apply() {
        ui.tabs().setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void release() {
        ui.tabs().setOnTabSelectedListener(null); //TODO check if we really need this
    }
}
