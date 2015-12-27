package com.deych.cookchooser.ui.meals;

import android.widget.Toast;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsActivityPresenter extends Presenter<MealsView> {

    private User mUser;
    private MealsModel mMealsModel;
    private UserModel mUserModel;
    private List<Category> mCategories = Collections.emptyList();
    private boolean mealsLoaded = false;

    @Inject
    public MealsActivityPresenter(User user, MealsModel mealsModel, UserModel userModel) {
        mUser = user;
        mMealsModel = mealsModel;
        mUserModel = userModel;
    }

    public void loadData() {

        if (view() != null) {
            view().bindUserData(mUser.getUsername(), mUser.getName());
        }

        if (!mealsLoaded) {
            mealsLoaded = true;
            Subscription mealsSubscription = mMealsModel
                    .getAllMealsFromNet()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {}, e -> {});
            addToSubscription(mealsSubscription);
        }

        if (!mCategories.isEmpty() && view() != null) {
            view().showCategories(mCategories);
            return;
        }

        Subscription subscription = mMealsModel
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (mCategories.size() > 0) {
                        return;
                    }
                    mCategories = Collections.unmodifiableList(list);
                    if (view() != null) {
                        view().showCategories(mCategories);
                    }
                }, e -> {
//                    if (adapter.getCount() > 0) {
//                        return;
//                    }
//                    Toast.makeText(MealsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                });
        addToSubscription(subscription);
    }

    public void logout() {
        //TODO clear all data
        mUserModel.logout();
        if (view() != null) {
            view().showLoginScreen();
        }
    }
}
