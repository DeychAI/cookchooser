package com.deych.cookchooser.ui.base.config.impl;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.deych.cookchooser.ui.base.config.FabUi;
import com.deych.cookchooser.ui.base.config.UiConfig;

/**
 * Created by deigo on 04.02.2016.
 */
public class FabConfig implements UiConfig {

    private FabUi ui;
    private boolean shouldShow;
    private View.OnClickListener listener;
    private int drawableRes = -1;

    public FabConfig(Context context) {
        try {
            ui = (FabUi) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FabUi");
        }
    }

    public FabConfig show() {
        this.shouldShow = true;
        return this;
    }

    public FabConfig hide() {
        this.shouldShow = false;
        return this;
    }

    public FabConfig listener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public FabConfig drawableRes(@DrawableRes int drawableRes) {
        this.drawableRes = drawableRes;
        return this;
    }

    public FloatingActionButton fab() {
        return ui.fab();
    }

    @Override
    public void apply() {
        ui.fab().setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        ui.fab().setOnClickListener(listener);
        if (shouldShow) {
            if (drawableRes == -1) {
                throw new IllegalStateException("Drawable Resource is not set!");
            }
            ui.fab().setImageResource(drawableRes);
        }
    }

    @Override
    public void release() {
        ui.fab().setOnClickListener(null);
    }
}
