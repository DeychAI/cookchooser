package com.deych.cookchooser.ui.meals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.MainActivityUiDelegate;
import com.deych.cookchooser.ui.base.Presenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deigo on 30.12.2015.
 */
public class MealsHostFragment extends BaseFragment implements MealsHostView {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private MealsPagerAdapter mMealsAdapter;

    @Inject
    MealsHostPresenter mPresenter;

    private MainActivityUiDelegate mainActivityUiDelegate;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getUserComponent().inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void setPresenter(Presenter aPresenter) {
        mPresenter = (MealsHostPresenter) aPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityUiDelegate = new MainActivityUiDelegate.Builder(context)
                .showFab()
                .showTabs()
                .setFabListener(v ->
                        Snackbar.make(v, "In a List!", Snackbar.LENGTH_LONG).setAction("Action", null).show())
                .build();
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
        mainActivityUiDelegate.onViewCreated();

        mMealsAdapter = new MealsPagerAdapter(getChildFragmentManager());
        mPresenter.bindView(this);
        mPresenter.loadData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unbindView(this);
        mainActivityUiDelegate.onDestroyView();

    }

    @Override
    public void showCategories(List<Category> categories) {
        if (mMealsAdapter.getCount() > 0) {
            return;
        }
        mMealsAdapter.setCategories(categories);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mMealsAdapter);
        mainActivityUiDelegate.getTabs().setupWithViewPager(viewPager);
    }
}
