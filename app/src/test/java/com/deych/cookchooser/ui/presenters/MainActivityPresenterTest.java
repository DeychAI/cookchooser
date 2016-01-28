package com.deych.cookchooser.ui.presenters;

import com.deych.cookchooser.MockRxSchedulerFactory;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.models.UserModel;
import com.deych.cookchooser.ui.MainActivityPresenter;
import com.deych.cookchooser.ui.MainActivityView;
import com.deych.cookchooser.util.RxSchedulerFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Observable;

import static org.mockito.Mockito.*;

/**
 * Created by deigo on 23.01.2016.
 */
public class MainActivityPresenterTest {

    private MainActivityPresenter presenter;

    private MainActivityView view;

    private User user;

    private UserModel userModel;

    private MealsModel mealsModel;

    @Before
    public void before() {
        user = mock(User.class);
        userModel = mock(UserModel.class);
        mealsModel = mock(MealsModel.class);
        view = mock(MainActivityView.class);

        presenter = new MainActivityPresenter(user, userModel, mealsModel, new MockRxSchedulerFactory());
    }

    @Test
    public void bindView() {
        presenter.bindView(view);
        verify(view).bindUserData(user.getUsername(), user.getName());
        verify(view).selectColor(userModel.getSelectedColor());
    }

    @Test
    public void updateColorLabels() {
        when(mealsModel.getMealsCountForColor(MealColor.None)).thenThrow(new RuntimeException());
        when(mealsModel.getMealsCountForColor(MealColor.Red)).thenReturn(Observable.just(10));
        when(mealsModel.getMealsCountForColor(MealColor.Green)).thenReturn(Observable.just(20));
        when(mealsModel.getMealsCountForColor(MealColor.Blue)).thenReturn(Observable.just(30));
        when(mealsModel.getMealsCountForColor(MealColor.Orange)).thenReturn(Observable.just(40));

        presenter.bindView(view);
        presenter.updateColorLabels();
        verify(view).updateColorCount(MealColor.Red, 10);
        verify(view).updateColorCount(MealColor.Green, 20);
        verify(view).updateColorCount(MealColor.Blue, 30);
        verify(view).updateColorCount(MealColor.Orange, 40);
    }

    @Test
    public void logout() {
        when(mealsModel.deleteAll()).thenReturn(Observable.just(true));
        presenter.bindView(view);
        presenter.logout();
        verify(userModel).logout();
        verify(view).showLoginScreen();
    }

    @Test
    public void colorSelected() {
        presenter.bindView(view);
        presenter.colorSelected(MealColor.Red);
        verify(userModel).colorSelected(MealColor.Red);
        verify(view).onColorSelected();
    }
}
