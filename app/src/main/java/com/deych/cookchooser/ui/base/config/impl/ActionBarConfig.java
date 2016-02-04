package com.deych.cookchooser.ui.base.config.impl;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.deych.cookchooser.ui.base.config.UiConfig;

/**
 * Created by deigo on 04.02.2016.
 */
public class ActionBarConfig implements UiConfig {

    private ActionBar actionBar;
    private CharSequence title;
    private int titleRes = -1;

    public ActionBarConfig(Context context) {
        try {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (activity.getSupportActionBar() == null) {
                throw new IllegalStateException("Activity must have ActionBar!");
            }
            actionBar = activity.getSupportActionBar();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must extends AppCompatActivity");
        }
    }

    public ActionBarConfig title(CharSequence title) {
        this.title = title;
        return this;
    }

    public ActionBarConfig title(@StringRes int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    @Override
    public void apply() {
        if (titleRes != -1) {
            actionBar.setTitle(titleRes);
        } else {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void release() {
    }
}
