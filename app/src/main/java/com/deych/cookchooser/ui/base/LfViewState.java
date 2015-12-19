package com.deych.cookchooser.ui.base;

import android.os.Bundle;

/**
 * Created by deigo on 19.12.2015.
 */
public class LfViewState<V extends LfView> implements ViewState<V> {
    protected static final int STATE_SHOW_FORM = 0;
    protected static final int STATE_SHOW_LOADING = 1;
    protected final String STATE_EXTRA = "bundle.state";

    protected int state = STATE_SHOW_FORM;

    public void setShowForm() {
        state = STATE_SHOW_FORM;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    @Override
    public void apply(V view) {
        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;
            case STATE_SHOW_FORM:
                view.showForm();
                break;
        }
    }

    @Override
    public void saveInstanceState(Bundle out) {
        out.putInt(STATE_EXTRA, state);
    }

    @Override
    public ViewState<V> restoreInstanceState(Bundle in) {
        if (in != null) {
            LfViewState<V> viewState = new LfViewState<V>();
            viewState.state = in.getInt(STATE_EXTRA);
            return viewState;
        }
        return null;
    }
}
