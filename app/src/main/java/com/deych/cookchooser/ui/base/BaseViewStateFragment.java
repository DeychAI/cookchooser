package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deigo on 17.12.2015.
 */
public abstract class BaseViewStateFragment extends BaseFragment implements ViewStateDelegateCallback {

    @Inject
    ViewStateDelegate mViewStateDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewStateDelegate.setDelegateCallback(this);
        mViewStateDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewStateDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewStateDelegate.onSaveInstanceState(outState);
    }
}
