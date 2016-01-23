package com.deych.cookchooser.ui.meals;

import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.ui.base.Presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsHostPresenter extends Presenter<MealsHostView> {

    private User user;
    private MealsModel mealsModel;
    private List<Category> categories = Collections.emptyList();
    private boolean mealsLoaded = false;
    private Random random = new Random();

    @Inject
    public MealsHostPresenter(User user, MealsModel mealsModel) {
        this.user = user;
        this.mealsModel = mealsModel;
    }

    public void loadData() {

        if (!mealsLoaded) {
            mealsLoaded = true;
            Subscription mealsSubscription = mealsModel
                    .loadAllMeals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                    }, e -> {
                    });
            addToSubscription(mealsSubscription);
        }

        if (!categories.isEmpty() && view() != null) {
            view().showCategories(categories);
            return;
        }

        Subscription subscription = mealsModel
                .reloadCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (categories.size() > 0) {
                        return;
                    }
                    categories = Collections.unmodifiableList(list);
                    if (view() != null) {
                        view().showCategories(categories);
                    }
                }, e -> {
//                    if (adapter.getCount() > 0) {
//                        return;
//                    }
//                    Toast.makeText(MealsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                });
        addToSubscription(subscription);
    }

    public void chooseOneMeal(long cat_id) {
        addToSubscription(mealsModel
                .getMealsFromDb(cat_id)
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    if (meals.isEmpty()) {
                        return;
                    }
                    Meal meal = meals.get(random.nextInt(meals.size()));
                    if (view() != null) {
                        view().showOneMeal(meal);
                    }
                }));
    }

    public void chooseFromAllCategories() {
        List<Meal> chosenMeals = new ArrayList<>();

        List<Observable<List<Meal>>> observables = new ArrayList<>();
        for (Category cat : categories) {
            observables.add(mealsModel.getMealsFromDb(cat.getId()).take(1));
        }

        Observable
                .concatEager(observables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Meal>>() {
                    @Override
                    public void onCompleted() {
                        if (view() != null) {
                            view().showFullMeals(chosenMeals);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Meal> meals) {
                        if (meals.isEmpty()) {
                            return;
                        }
                        Meal meal = meals.get(random.nextInt(meals.size()));
                        chosenMeals.add(meal);
                    }
                });

    }
}
