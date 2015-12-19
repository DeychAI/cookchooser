package com.deych.cookchooser.ui.base;

/**
 * Created by deigo on 17.12.2015.
 */
public interface PresenterCacheDelegateCallback {
    Presenter onEmptyCache();
    void restoredFromCache(Presenter aPresenter);
    void onCacheCleared();
}
