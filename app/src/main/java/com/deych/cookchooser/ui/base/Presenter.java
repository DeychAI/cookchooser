package com.deych.cookchooser.ui.base;

/**
 * Created by deigo on 16.12.2015.
 */
public abstract class Presenter<V> {
    private volatile V mView;

    public void bindView(V aView) {
        if (mView != null) {
            throw new IllegalStateException("View is not unbounded!");
        }
        mView = aView;
    }

    public void unbindView(V aView) {
        if (mView != aView) {
            throw new IllegalStateException("Bounded some other view!");
        }
        mView = null;
    }

    protected V view() {
        return mView;
    }
}
