package com.deych.cookchooser.ui.meals.detail;

import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.util.RxSchedulerFactory;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 04.01.2016.
 */
public class MealsDetailPresenter extends Presenter<MealsDetailView>{

    private MealsModel mealsModel;
    private RxSchedulerFactory rxSchedulerFactory;

    @Inject
    public MealsDetailPresenter(MealsModel mealsModel, RxSchedulerFactory rxSchedulerFactory) {
        this.mealsModel = mealsModel;
        this.rxSchedulerFactory = rxSchedulerFactory;
    }

    public void loadCategories() {
        addToUnbindSubscription(mealsModel.getCategoriesFromDb()
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread())
                .subscribe(categories -> {
                   if (view() != null) {
                       view().setCategories(categories);
                   }
                }));
    }
}
