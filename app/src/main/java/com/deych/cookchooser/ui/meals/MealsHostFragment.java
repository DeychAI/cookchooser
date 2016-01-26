package com.deych.cookchooser.ui.meals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.uicontrols.MainUiDelegate;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.meals.dialog.ChooseFullDialog;
import com.deych.cookchooser.ui.meals.edit.EditMealActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 30.12.2015.
 */
public class MealsHostFragment extends BaseFragment implements MealsHostView {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private MealsPagerAdapter mealsPagerAdapter;

    @Inject
    MealsHostPresenter presenter;

    private MainUiDelegate mainUiDelegate;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getUserComponent().inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void setPresenter(Presenter presenter) {
        this.presenter = (MealsHostPresenter) presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meals, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainUiDelegate = new MainUiDelegate.Builder(getActivity())
                .showFab()
                .showTabs()
                .setToolbarTitle(R.string.title_list)
                .setFabDrawable(R.drawable.ic_help)
                .setFabListener(v -> fabChoose())
                .build();

        mainUiDelegate.onViewCreated();

        mealsPagerAdapter = new MealsPagerAdapter(getChildFragmentManager());
        presenter.bindView(this);
        presenter.loadData();

    }

    private void fabChoose() {
        presenter.chooseOneMeal(mealsPagerAdapter.getCategory(viewPager.getCurrentItem()).getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meals, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                EditMealActivity.startForAdding(getContext(),
                        mealsPagerAdapter.getCategory(viewPager.getCurrentItem()).getId());
                return true;
            case R.id.action_choose:
                menuChoose();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void menuChoose() {
        presenter.chooseFromAllCategories();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(this);
        mainUiDelegate.onDestroyView();

    }

    @Override
    public void showCategories(List<Category> categories) {
        if (mealsPagerAdapter.getCount() > 0) {
            return;
        }
        mealsPagerAdapter.setCategories(categories);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mealsPagerAdapter);
        mainUiDelegate.getTabs().setupWithViewPager(viewPager);
    }

    @Override
    public void showOneMeal(Meal meal) {
        mainUiDelegate.createSnackbar(meal.getName(), Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void showFullMeals(List<Meal> meals) {
        ChooseFullDialog.newInstance(TextUtils.join("\n", meals)).show(getFragmentManager(), "dialog");
    }
}
