package com.deych.cookchooser.ui.base;

import android.content.Context;
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

    public TabLayout getTabs() {
        return activity.getTabs();
    }

    public void onViewCreated() {
        activity.getFab().setVisibility(showFab ? View.VISIBLE : View.GONE);
        activity.getFab().setOnClickListener(fabListener);
        activity.getTabs().setVisibility(showTabs ? View.VISIBLE : View.GONE);
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
    }

    public static class Builder {

        private MainActivity activity;

        private boolean showFab = false;
        private View.OnClickListener fabListener = null;
        private boolean showTabs = false;

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

        public MainActivityUiDelegate build() {
            return new MainActivityUiDelegate(this);
        }
    }

}
