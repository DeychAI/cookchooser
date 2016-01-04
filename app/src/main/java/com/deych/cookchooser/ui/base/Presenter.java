package com.deych.cookchooser.ui.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by deigo on 16.12.2015.
 */
public abstract class Presenter<V> {
    private volatile V view;

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CompositeSubscription unbindSubscription = new CompositeSubscription();

    @CallSuper
    public void bindView(V view) {
        if (this.view != null) {
            throw new IllegalStateException("View is not unbounded!");
        }
        this.view = view;
    }

    @CallSuper
    public void unbindView(V view) {
        if (this.view != view) {
            throw new IllegalStateException("Bounded some other view!");
        }
        this.view = null;
        unbindSubscription.clear();
    }

    protected V view() {
        return view;
    }

    protected final void addToSubscription(@NonNull Subscription subscription, @NonNull Subscription... subscriptions) {
        compositeSubscription.add(subscription);

        for (Subscription s : subscriptions) {
            compositeSubscription.add(s);
        }
    }

    protected final void addToUnbindSubscription(@NonNull Subscription subscription, @NonNull Subscription... subscriptions) {
        unbindSubscription.add(subscription);

        for (Subscription s : subscriptions) {
            unbindSubscription.add(s);
        }
    }

    public void clearSubscription() {
        compositeSubscription.clear();
    }
}
