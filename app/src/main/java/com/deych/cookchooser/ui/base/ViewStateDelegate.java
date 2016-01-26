package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by deigo on 17.12.2015.
 */
public class ViewStateDelegate {

    private boolean shouldApplyViewState;
    private ViewState viewState;
    private ViewStateDelegateCallback callback;

    @Inject
    public ViewStateDelegate(ViewState viewState) {
        this.viewState = viewState;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        shouldApplyViewState = false;
        if (savedInstanceState != null) {
            ViewState restoredState = viewState.restoreInstanceState(savedInstanceState);
            if (restoredState != null) {
                viewState = restoredState;
                shouldApplyViewState = true;
            }
        }

    }

    public void onActivityCreated() {
        if (shouldApplyViewState) {
            callback.applyViewState(viewState);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        viewState.saveInstanceState(outState);
    }

    public void setDelegateCallback(ViewStateDelegateCallback callback) {
        this.callback = callback;
    }
}
