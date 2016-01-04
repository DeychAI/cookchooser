package com.deych.cookchooser.ui.meals.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.util.Extras;
import com.farbod.labelledspinner.LabelledSpinner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 04.01.2016.
 */
public class MealsDetailFragment extends BaseFragment implements MealsDetailView{

    private static final String ADDING_MEAL = "extra.adding.meal";

    //New Meal
    public static MealsDetailFragment newInstanceAddMeal(long category_id) {
        MealsDetailFragment fragment = new MealsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Extras.EXTRA_CATEGORY_ID, category_id);
        args.putBoolean(ADDING_MEAL, true);
        fragment.setArguments(args);
        return fragment;
    }

    //Existing Meal
    public static MealsDetailFragment newInstanceDetails(long meal_id) {
        MealsDetailFragment fragment = new MealsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Extras.EXTRA_MEAL_ID, meal_id);
        args.putBoolean(ADDING_MEAL, false);
        fragment.setArguments(args);
        return fragment;
    }

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
        this.presenter = (MealsDetailPresenter) presenter;
    }

    @Inject
    MealsDetailPresenter presenter;

    @Bind(R.id.colorGroup)
    RadioGroup colorGroup;

    @Bind(R.id.spCategory)
    LabelledSpinner spCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meals_detail, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bindView(this);
        presenter.loadCategories();

        colorGroup.check(R.id.btnNone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(this);
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setCategories(List<Category> categories) {
        spCategory.setItemsArray(new ArrayList<>(categories));
    }
}
