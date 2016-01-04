package com.deych.cookchooser.ui.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.deych.cookchooser.ui.MainActivity;

/**
 * Created by deigo on 30.12.2015.
 */
public class MainActivityUiDelegate {

    private MainActivity activity;

    private boolean showFab;
    private View.OnClickListener fabListener;
    private boolean showTabs;
    private int fabDrawableRes;
    private int toolbarTitleRes;

    public TabLayout getTabs() {
        return activity.getTabs();
    }

    public void onViewCreated() {
        activity.getFab().setVisibility(showFab ? View.VISIBLE : View.GONE);
        activity.getFab().setOnClickListener(fabListener);
        activity.getTabs().setVisibility(showTabs ? View.VISIBLE : View.GONE);
        activity.getToolbar().setTitle(toolbarTitleRes);
        if (showFab) {
            activity.getFab().setImageResource(fabDrawableRes);
        }
    }

    public void onDestroyView() {
        activity.getTabs().setOnTabSelectedListener(null);
        activity.getFab().setOnClickListener(null);
        activity = null;
    }

    private MainActivityUiDelegate(Builder builder) {
        this.activity = builder.activity;
        this.showFab = builder.showFab;
        this.fabListener = builder.fabListener;
        this.showTabs = builder.showTabs;
        this.fabDrawableRes = builder.fabDrawableRes;
        this.toolbarTitleRes = builder.toolbarTitleRes;
    }

    public static class Builder {

        private MainActivity activity;

        private boolean showFab = false;
        private View.OnClickListener fabListener = null;
        private boolean showTabs = false;
        private int fabDrawableRes = -1;
        private int toolbarTitleRes = -1;

        public Builder(Context context) {
            try {
                activity = (MainActivity) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must be MainActivity");
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

        public MainActivityUiDelegate build() {
            if (toolbarTitleRes == -1) {
                throw new IllegalStateException("You must set title for the toolbar!");
            }
            if (showFab && fabDrawableRes == -1) {
                throw new IllegalStateException("You must set drawable if you want to show FAB!");
            }
            return new MainActivityUiDelegate(this);
        }
    }

}
