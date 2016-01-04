package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 20.12.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

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

    protected abstract void setPresenter(Presenter aPresenter);

    protected abstract void bindViews();

    public CompositeSubscription getUiSubscription() {
        return uiSubscription;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponents();
        cacheDelegate.setDelegateCallback(delegateCallback);
        cacheDelegate.onCreate(savedInstanceState);
        bindViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        destroyedBySystem = true;
        cacheDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        destroyedBySystem = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiSubscription.clear();
        cacheDelegate.onDestroy(destroyedBySystem);
        delegateCallback = null;
    }
}
