package com.deych.cookchooser.ui.base;

import android.os.Bundle;

/**
 * Created by deigo on 16.12.2015.
 */
public interface ViewState<V> {
    void apply(V view);
    void saveInstanceState(Bundle out);
    ViewState<V> restoreInstanceState(Bundle in);
    void setState(int state);
    int getState();
}
