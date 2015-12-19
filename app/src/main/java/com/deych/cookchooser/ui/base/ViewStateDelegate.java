package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by deigo on 17.12.2015.
 */
public class ViewStateDelegate {

    private boolean shouldApplyViewState;
    private ViewState mViewState;
    private ViewStateDelegateCallback mCallback;

    public ViewStateDelegate(ViewState aViewState) {
        mViewState = aViewState;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        shouldApplyViewState = false;
        if (savedInstanceState != null) {
            ViewState restoredState = mViewState.restoreInstanceState(savedInstanceState);
            if (restoredState != null) {
                mViewState = restoredState;
                shouldApplyViewState = true;
            }
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (shouldApplyViewState) {
            mCallback.applyViewState(mViewState);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        mViewState.saveInstanceState(outState);
    }

    public void setDelegateCallback(ViewStateDelegateCallback aCallback) {
        mCallback = aCallback;
    }
}
