package com.deych.cookchooser.ui.meals;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.MealsModel;
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
public class MealsListPresenter extends Presenter<MealsListView> {

    private MealsModel mMealsModel;
    private User mUser;
    private List<Meal> mMeals = Collections.emptyList();

    @Inject
    public MealsListPresenter(MealsModel mealsModel, User user) {
        mMealsModel = mealsModel;
        mUser = user;
    }

    public void refreshMeals(long category_id) {
        Subscription subscription = mMealsModel.getMealsFromNet(category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if(view() != null){
                        view().hideRefresh();
//                        view().showMeals(list);
                    }
                }, e -> {
                    if(view() != null){
                        view().hideRefresh();
                    }
                });
        addToSubscription(subscription);
    }

    public void loadMeals(long category_id) {
        Subscription subscription = mMealsModel.getMealsFromDb(category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    if(view() != null){
                        view().showMeals(l);
                    }
                }, e -> {
                    Timber.v("Error " + e);
                });
        addToUnbindSubscription(subscription);
    }
}
