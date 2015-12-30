package com.deych.cookchooser.ui.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 16.12.2015.
 */
public abstract class Presenter<V> {
    private volatile V mView;

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private final CompositeSubscription mUnbindSubscription = new CompositeSubscription();

    @CallSuper
    public void bindView(V aView) {
        if (mView != null) {
            throw new IllegalStateException("View is not unbounded!");
        }
        mView = aView;
    }

    @CallSuper
    public void unbindView(V aView) {
        if (mView != aView) {
            throw new IllegalStateException("Bounded some other view!");
        }
        mView = null;
        mUnbindSubscription.clear();
    }

    protected V view() {
        return mView;
    }

    protected final void addToSubscription(@NonNull Subscription subscription, @NonNull Subscription... subscriptions) {
        mCompositeSubscription.add(subscription);

        for (Subscription s : subscriptions) {
            mCompositeSubscription.add(s);
        }
    }

    protected final void addToUnbindSubscription(@NonNull Subscription subscription, @NonNull Subscription... subscriptions) {
        mUnbindSubscription.add(subscription);

        for (Subscription s : subscriptions) {
            mUnbindSubscription.add(s);
        }
    }

    public void clearSubscription() {
        mCompositeSubscription.clear();
    }
}
