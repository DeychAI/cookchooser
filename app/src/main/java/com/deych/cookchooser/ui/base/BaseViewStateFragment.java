package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by deigo on 17.12.2015.
 */
public abstract class BaseViewStateFragment extends BaseFragment implements ViewStateDelegateCallback {

    @Inject
    ViewStateDelegate viewStateDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewStateDelegate.setDelegateCallback(this);
        viewStateDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewStateDelegate.onActivityCreated();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewStateDelegate.onSaveInstanceState(outState);
    }
}
