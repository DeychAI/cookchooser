package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 17.12.2015.
 */
public abstract class BaseFragment extends Fragment {

    private CompositeSubscription uiSubscription = new CompositeSubscription();
    private boolean destroyedBySystem;

    @Inject
    PresenterCacheDelegate cacheDelegate;

    PresenterCacheDelegateCallback delegateCallback = new PresenterCacheDelegateCallback() {
        @Override
        public Presenter onEmptyCache() {
            return getPresenter();
        }

        @Override
        public void restoredFromCache(Presenter presenter) {
            setPresenter(presenter);
        }

        @Override
        public void onCacheCleared() {
            getPresenter().clearSubscription();
        }
    };

    protected abstract void setUpComponents();

    protected abstract Presenter getPresenter();

    protected abstract void setPresenter(Presenter presenter);

    public CompositeSubscription getUiSubscription() {
        return uiSubscription;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponents();
        cacheDelegate.setDelegateCallback(delegateCallback);
        cacheDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        destroyedBySystem = true;
        cacheDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        destroyedBySystem = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cacheDelegate.onDestroy(destroyedBySystem);
        delegateCallback = null;
    }
}
