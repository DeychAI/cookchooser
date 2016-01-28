package com.deych.cookchooser.ui;

import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.util.RxSchedulerFactory;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deigo on 30.12.2015.
 */
public class MainActivityPresenter extends Presenter<MainActivityView> {

    private User user;
    private UserModel userModel;
    private MealsModel mealsModel;
    private RxSchedulerFactory rxSchedulerFactory;

    @Inject
    public MainActivityPresenter(User user, UserModel userModel, MealsModel mealsModel,
                                 RxSchedulerFactory rxSchedulerFactory) {
        this.user = user;
        this.userModel = userModel;
        this.mealsModel = mealsModel;
        this.rxSchedulerFactory = rxSchedulerFactory;
    }

    @Override
    public void bindView(MainActivityView view) {
        super.bindView(view);
        if (view() != null) {
            view().bindUserData(user.getUsername(), user.getName());
            view().selectColor(userModel.getSelectedColor());
        }
    }

    public void updateColorLabels() {
        for (MealColor mealColor : MealColor.values()) {
            if (mealColor.equals(MealColor.None)) {
                continue;
            }
            addToUnbindSubscription(mealsModel
                    .getMealsCountForColor(mealColor)
                    .subscribeOn(rxSchedulerFactory.io())
                    .observeOn(rxSchedulerFactory.mainThread())
                    .subscribe(count -> {
                        if (view() != null) {
                            view().updateColorCount(mealColor, count);
                        }
                    }));
        }
    }

    public void logout() {
        mealsModel.deleteAll()
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread())
                .subscribe(result -> {
                    userModel.logout();
                    if (view() != null) {
                        view().showLoginScreen();
                    }
                });
    }

    public void colorSelected(MealColor color) {
        userModel.colorSelected(color);
        if (view() != null) {
            view().onColorSelected();
        }
    }
}
