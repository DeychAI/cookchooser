package com.deych.cookchooser.ui.meals.edit;

import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 18.01.2016.
 */
public class EditMealPresenter extends Presenter<EditMealView> {

    private UserModel userModel;
    private MealsModel mealsModel;

    private Meal meal;

    @Inject
    public EditMealPresenter(UserModel userModel, MealsModel mealsModel) {
        this.userModel = userModel;
        this.mealsModel = mealsModel;
    }

    public void bindData(String uuid, long categoryId) {
        if (meal !=  null){
            view().setTitle(meal.getName());
            loadCategories();
            return;
        }
        Observable<Meal> mealObservable;
        if (uuid == null) {
            //Adding
            Meal newMeal = new Meal();
            newMeal.setCategoryId(categoryId);
            newMeal.setColor(userModel.getSelectedColor());
            mealObservable = Observable.just(newMeal);
        } else {
            //Editing
            mealObservable = mealsModel.getMeal(uuid);
        }
        mealObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    meal = result;
                    loadCategories();
                    if (view() != null) {
                        view().setTitle(meal.getName());
                        view().setMealData(meal);
                    }
                });
    }

    private void loadCategories() {
        addToUnbindSubscription(mealsModel.getCategoriesFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (view() != null) {
                        view().setCategories(categories, meal.getCategoryId());
                    }
                }));
    }

    public void save(String name, long categoryId, MealColor color) {
        meal.setName(name);
        meal.setCategoryId(categoryId);
        meal.setColor(color);
        mealsModel.saveMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (view() != null) {
                        if (result) {
                            view().onSaved();
                        } else {
                            view().showError();
                        }
                    }
                }, e -> {
                    if (view() != null) {
                        view().showError();
                    }
                });
    }
}
