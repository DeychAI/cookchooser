package com.deych.cookchooser.ui.base;

import android.os.Bundle;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by deigo on 17.12.2015.
 */
public class PresenterCacheDelegate {

    private static final String PRESENTER_INDEX_KEY = "presenter-index";

    private PresenterCacheDelegateCallback delegateCallback;
    private PresenterCache cache;
    private long presenterId;

    @Inject
    public PresenterCacheDelegate(PresenterCache cache) {
        this.cache = cache;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            presenterId = cache.generateId();
        } else {
            presenterId = savedInstanceState.getLong(PRESENTER_INDEX_KEY, -1);
//            if (presenterId < 0) {
//                presenterId = cache.generateId();
//            }
        }
        Presenter presenter = cache.get(presenterId);
        if(presenter == null) {
//            if (savedInstanceState != null) {
                //It seems that our app was destroyed by Android without onDestroy() calls. We need to shift ID
//                presenterId = cache.generateId();
//            }
            cache.put(presenterId, delegateCallback.onEmptyCache());
        } else {
            delegateCallback.restoredFromCache(presenter);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Timber.d("onSaveInstanceState");
        outState.putLong(PRESENTER_INDEX_KEY, presenterId);
    }


    public void onDestroy(boolean destroyedBySystem) {
        if (!destroyedBySystem) {
            cache.put(presenterId, null);
            delegateCallback.onCacheCleared();
        }
    }

    public void setDelegateCallback(PresenterCacheDelegateCallback delegateCallback) {
        this.delegateCallback = delegateCallback;
    }
}
