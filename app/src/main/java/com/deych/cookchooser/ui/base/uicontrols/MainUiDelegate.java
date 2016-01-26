package com.deych.cookchooser.ui.base.uicontrols;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.View;

/**
 * Created by deigo on 30.12.2015.
 */
public class MainUiDelegate {

    private MainUi ui;

    private boolean showFab;
    private View.OnClickListener fabListener;
    private boolean showTabs;
    private int fabDrawableRes;
    private int toolbarTitleRes;

    public TabLayout getTabs() {
        return ui.tabs();
    }

    public static Builder builder(Context context){
        return new Builder(context);
    }

    public void onViewCreated() {
        ui.fab().setVisibility(showFab ? View.VISIBLE : View.GONE);
        ui.fab().setOnClickListener(fabListener);
        ui.tabs().setVisibility(showTabs ? View.VISIBLE : View.GONE);
        ui.toolbar().setTitle(toolbarTitleRes);
        if (showFab) {
            ui.fab().setImageResource(fabDrawableRes);
        }
    }

    public void onDestroyView() {
        ui.tabs().setOnTabSelectedListener(null);
        ui.fab().setOnClickListener(null);
        ui = null;
    }

    public Snackbar createSnackbar(@NonNull CharSequence text, int duration) {
        return Snackbar.make(ui.toolbar(), text, duration);
    }

    public Snackbar createSnackbar(@StringRes int resId, int duration) {
        return Snackbar.make(ui.toolbar(), resId, duration);
    }

    private MainUiDelegate(Builder builder) {
        this.ui = builder.ui;
        this.showFab = builder.showFab;
        this.fabListener = builder.fabListener;
        this.showTabs = builder.showTabs;
        this.fabDrawableRes = builder.fabDrawableRes;
        this.toolbarTitleRes = builder.toolbarTitleRes;
    }

    public static class Builder {

        private MainUi ui;

        private boolean showFab = false;
        private View.OnClickListener fabListener = null;
        private boolean showTabs = false;
        private int fabDrawableRes = -1;
        private int toolbarTitleRes = -1;

        public Builder(Context context) {
            try {
                ui = (MainUi) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement MainUi");
            }
        }

        public Builder showFab() {
            this.showFab = true;
            return this;
        }

        public Builder setFabListener(View.OnClickListener fabListener) {
            this.fabListener = fabListener;
            return this;
        }

        public Builder showTabs() {
            this.showTabs = true;
            return this;
        }

        public Builder setFabDrawable(@DrawableRes int icon) {
            this.fabDrawableRes = icon;
            return this;
        }

        public Builder setToolbarTitle(@StringRes int resId) {
            this.toolbarTitleRes = resId;
            return this;
        }

        public MainUiDelegate build() {
            if (toolbarTitleRes == -1) {
                throw new IllegalStateException("You must set title for the toolbar!");
            }
            if (showFab && fabDrawableRes == -1) {
                throw new IllegalStateException("You must set drawable if you want to show FAB!");
            }
            return new MainUiDelegate(this);
        }
    }

}
