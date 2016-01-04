package com.deych.cookchooser.ui.meals.detail;

import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 04.01.2016.
 */
public class MealsDetailPresenter extends Presenter<MealsDetailView>{

    private MealsModel mealsModel;

    @Inject
    public MealsDetailPresenter(MealsModel mealsModel) {
        this.mealsModel = mealsModel;
    }

    public void loadCategories() {
        addToUnbindSubscription(mealsModel.getCategoriesFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                   if (view() != null) {
                       view().setCategories(categories);
                   }
                }));
    }
}
