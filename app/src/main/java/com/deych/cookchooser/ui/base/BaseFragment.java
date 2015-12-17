package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Created by deigo on 17.12.2015.
 */
public abstract class BaseFragment extends Fragment implements PresenterCacheDelegateCallback{

    private boolean mDestroyedBySystem;

    @Inject
    PresenterCacheDelegate mCacheDelegate;

    protected abstract void setUpComponents();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponents();
        mCacheDelegate.setDelegateCallback(this);
        mCacheDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDestroyedBySystem = true;
        mCacheDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDestroyedBySystem = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCacheDelegate.onDestroy(mDestroyedBySystem);
    }

}
