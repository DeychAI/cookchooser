package com.deych.cookchooser.ui.base;

import android.os.Bundle;

import javax.inject.Inject;

/**
 * Created by deigo on 17.12.2015.
 */
public class PresenterCacheDelegate {

    private static final String PRESENTER_INDEX_KEY = "presenter-index";

    private PresenterCacheDelegateCallback mDelegateCallback;
    private PresenterCache mCache;
    private long mPresenterId;

    @Inject
    public PresenterCacheDelegate(PresenterCache aCache) {
        mCache = aCache;
    }

    public void onCreate(Bundle aSavedInstanceState) {
        if (aSavedInstanceState == null) {
            mPresenterId = mCache.generateId();
        } else {
            mPresenterId = aSavedInstanceState.getLong(PRESENTER_INDEX_KEY);
        }
        Presenter presenter = mCache.get(mPresenterId);
        if(presenter == null) {
            mCache.put(mPresenterId, mDelegateCallback.onEmptyCache());
        } else {
            mDelegateCallback.restoredFromCache(presenter);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(PRESENTER_INDEX_KEY, mPresenterId);
    }


    public void onDestroy(boolean aDestroyedBySystem) {
        if (!aDestroyedBySystem) {
            mCache.put(mPresenterId, null);
        }
    }

    public void setDelegateCallback(PresenterCacheDelegateCallback aDelegateCallback) {
        mDelegateCallback = aDelegateCallback;
    }
}
