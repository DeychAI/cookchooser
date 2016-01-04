package com.deych.cookchooser.ui.meals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.MainActivityUiDelegate;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.meals.detail.AddMealActivity;

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

    private MainActivityUiDelegate mainActivityUiDelegate;

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
        mainActivityUiDelegate = new MainActivityUiDelegate.Builder(getActivity())
                .showFab()
                .showTabs()
                .setToolbarTitle(R.string.title_list)
                .setFabDrawable(R.drawable.ic_help)
                .setFabListener(v ->
                        Snackbar.make(v, "In a List!", Snackbar.LENGTH_LONG).setAction("Action", null).show())
                .build();

        mainActivityUiDelegate.onViewCreated();

        mealsPagerAdapter = new MealsPagerAdapter(getChildFragmentManager());
        presenter.bindView(this);
        presenter.loadData();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meals, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), AddMealActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(this);
        mainActivityUiDelegate.onDestroyView();

    }

    @Override
    public void showCategories(List<Category> categories) {
        if (mealsPagerAdapter.getCount() > 0) {
            return;
        }
        mealsPagerAdapter.setCategories(categories);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mealsPagerAdapter);
        mainActivityUiDelegate.getTabs().setupWithViewPager(viewPager);
    }
}
