package com.deych.cookchooser.ui.base.uicontrols;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by deigo on 03.02.2016.
 */
public class ToolbarUiDelegate {

    protected Toolbar toolbar;
    protected int toolbarTitleRes;
    protected String toolbarTitle;

    protected ToolbarUiDelegate(Builder builder) {
        this.toolbar = builder.toolbar;
        this.toolbarTitleRes = builder.toolbarTitleRes;
        this.toolbarTitle = builder.toolbarTitle;
    }

    protected void setToolbarTitle() {
        if (toolbarTitleRes != -1) {
            toolbar.setTitle(toolbarTitleRes);
        } else {
            toolbar.setTitle(toolbarTitle);
        }
    }

    public void onViewCreated() {
        setToolbarTitle();
    }

    public void onDestroyView() {
    }

    public void onResume() {
        setToolbarTitle();
    }

    public static class Builder {

        private Toolbar toolbar;
        private int toolbarTitleRes = -1;
        private String toolbarTitle;

        public Builder(Context context) {
            try {
                ToolbarUi toolbarUi = (ToolbarUi) context;
                toolbar = toolbarUi.toolbar();
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement FabToolbarUi");
            }
        }

        public Builder setToolbarTitle(@StringRes int resId) {
            this.toolbarTitleRes = resId;
            return this;
        }

        public Builder setToolbarTitle(String title) {
            this.toolbarTitle = title;
            return this;
        }

        public ToolbarUiDelegate build() {
            if (toolbarTitleRes == -1 && TextUtils.isEmpty(toolbarTitle)) {
                throw new IllegalStateException("You must set title for the toolbar!");
            }
            return new ToolbarUiDelegate(this);
        }
    }

}
