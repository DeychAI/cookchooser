package com.deych.cookchooser.ui.base.ui_controls;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by alexanderdeych on 13.01.16.
 */
public class FabToolbarUiDelegate {

    private FabToolbarUi ui;

    private boolean showFab;
    private View.OnClickListener fabListener;
    private int fabDrawableRes;
    private int toolbarTitleRes;

    public void onViewCreated() {
        ui.fab().setVisibility(showFab ? View.VISIBLE : View.GONE);
        ui.fab().setOnClickListener(fabListener);
        ui.toolbar().setTitle(toolbarTitleRes);
        if (showFab) {
            ui.fab().setImageResource(fabDrawableRes);
        }
    }

    public void onDestroyView() {
        ui.fab().setOnClickListener(null);
        ui = null;
    }

    private FabToolbarUiDelegate(Builder builder) {
        this.ui = builder.ui;
        this.showFab = builder.showFab;
        this.fabListener = builder.fabListener;
        this.fabDrawableRes = builder.fabDrawableRes;
        this.toolbarTitleRes = builder.toolbarTitleRes;

    }

    public static class Builder {

        private FabToolbarUi ui;

        private boolean showFab = false;
        private View.OnClickListener fabListener = null;
        private int fabDrawableRes = -1;
        private int toolbarTitleRes = -1;

        public Builder(Context context) {
            try {
                ui = (FabToolbarUi) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement FabToolbarUi");
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

        public Builder setFabDrawable(@DrawableRes int icon) {
            this.fabDrawableRes = icon;
            return this;
        }

        public Builder setToolbarTitle(@StringRes int resId) {
            this.toolbarTitleRes = resId;
            return this;
        }

        public FabToolbarUiDelegate build() {
            if (toolbarTitleRes == -1) {
                throw new IllegalStateException("You must set title for the toolbar!");
            }
            if (showFab && fabDrawableRes == -1) {
                throw new IllegalStateException("You must set drawable if you want to show FAB!");
            }
            return new FabToolbarUiDelegate(this);
        }
    }

}
