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

    private MealsModel mealsModel;
    private User user;
    private List<Meal> meals = Collections.emptyList();
    private long id;
    private Subscription mealsSubscription;

    @Inject
    public MealsListPresenter(MealsModel mealsModel, User user) {
        this.mealsModel = mealsModel;
        this.user = user;
    }

    public void refreshMeals() {
        Subscription subscription = mealsModel.getMealsFromNet(id)
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

    public void loadMeals() {
        if (mealsSubscription != null) {
            mealsSubscription.unsubscribe();
        }
        mealsSubscription = mealsModel.getMealsFromDb(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    if(view() != null){
                        view().showMeals(l);
                    }
                }, e -> {
                    Timber.v("Error " + e);
                });
    }

    @Override
    public void unbindView(MealsListView view) {
        super.unbindView(view);
        if (mealsSubscription != null) {
            mealsSubscription.unsubscribe();
        }
    }

    public void setCategoryId(long id) {
        this.id = id;
    }

    public void deleteMeal(Meal meal) {
        mealsModel.deleteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
