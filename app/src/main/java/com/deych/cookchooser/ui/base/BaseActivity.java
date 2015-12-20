package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 20.12.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private CompositeSubscription mUiSubscription = new CompositeSubscription();
    private boolean mDestroyedBySystem;

    @Inject
    PresenterCacheDelegate mCacheDelegate;

    PresenterCacheDelegateCallback mDelegateCallback = new PresenterCacheDelegateCallback() {
        @Override
        public Presenter onEmptyCache() {
            return getPresenter();
        }

        @Override
        public void restoredFromCache(Presenter aPresenter) {
            setPresenter(aPresenter);
        }

        @Override
        public void onCacheCleared() {
            getPresenter().clearSubscription();
        }
    };

    protected abstract void setUpComponents();
    protected abstract Presenter getPresenter();
    protected abstract void setPresenter(Presenter aPresenter);

    public CompositeSubscription getUiSubscription() {
        return mUiSubscription;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponents();
        mCacheDelegate.setDelegateCallback(mDelegateCallback);
        mCacheDelegate.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDestroyedBySystem = true;
        mCacheDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDestroyedBySystem = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiSubscription.clear();
        mCacheDelegate.onDestroy(mDestroyedBySystem);
        mDelegateCallback = null;
    }
}
