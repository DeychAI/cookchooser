package com.deych.cookchooser.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.deych.cookchooser.App;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 17.12.2015.
 */
public abstract class BaseFragment extends Fragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponents();
        mCacheDelegate.setDelegateCallback(mDelegateCallback);
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
    public void onDestroyView() {
        super.onDestroyView();
        mUiSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCacheDelegate.onDestroy(mDestroyedBySystem);
        mDelegateCallback = null;
    }
}
