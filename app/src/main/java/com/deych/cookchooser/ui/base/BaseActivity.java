package com.deych.cookchooser.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.login.LoginFragment;
import com.deych.cookchooser.ui.meals.MealsActivity;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
    protected abstract void bindViews();

    public CompositeSubscription getUiSubscription() {
        return mUiSubscription;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.get(this).getUserComponent() != null) {
            setUpComponents();
            mCacheDelegate.setDelegateCallback(mDelegateCallback);
            mCacheDelegate.onCreate(savedInstanceState);
            bindViews();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDestroyedBySystem = true;
        if (mCacheDelegate != null) {
            mCacheDelegate.onSaveInstanceState(outState);
        }
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
        if (mCacheDelegate != null) {
            mCacheDelegate.onDestroy(mDestroyedBySystem);
        }
        mDelegateCallback = null;
    }
}
