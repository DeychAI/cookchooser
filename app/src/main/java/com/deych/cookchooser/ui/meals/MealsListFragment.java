package com.deych.cookchooser.ui.meals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.meals.edit.EditMealActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsListFragment extends BaseFragment implements MealsListView {

    private static final String EXTRA_ID = "id";
    public static final String ACTION_FILTER_MEALS = "cookchooser.action.filter.meals";

    private BroadcastReceiver filterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.loadMeals();
        }
    };

    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private MealsAdapter adapter;

    @Inject
    MealsListPresenter presenter;

    public static MealsListFragment newInstance(long category_id) {
        Bundle args = new Bundle();
        args.putLong(EXTRA_ID, category_id);
        MealsListFragment fragment = new MealsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, v);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MealsAdapter(meal -> EditMealActivity.startForEditing(getContext(), meal.getUuid()));
        list.setAdapter(adapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.bindView(this);
        presenter.setCategoryId(getArguments().getLong(EXTRA_ID));
        presenter.loadMeals();
        refreshLayout.setOnRefreshListener(presenter::refreshMeals);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(filterReceiver
                , new IntentFilter(ACTION_FILTER_MEALS));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(filterReceiver);
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
        this.presenter = (MealsListPresenter) presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.unbindView(this);
        Timber.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.get(getContext()).getRefWatcher().watch(this);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        hideRefresh();
        adapter.setList(meals);
    }

    @Override
    public void hideRefresh() {
        refreshLayout.setRefreshing(false);
    }
}