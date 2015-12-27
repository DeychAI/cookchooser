package com.deych.cookchooser.ui.meals;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.models.MealsModel;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsListFragment extends BaseFragment implements MealsListView{

    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private MealsAdapter mAdapter;

    @Inject
    MealsListPresenter mPresenter;

    public static MealsListFragment newInstance(long category_id) {
        Bundle args = new Bundle();
        args.putLong("id", category_id);
        MealsListFragment fragment = new MealsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private long id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);

        id = getArguments().getLong("id");

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MealsAdapter();
        list.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.bindView(this);
            mPresenter.loadMeals(id);
            refreshLayout.setOnRefreshListener(() -> mPresenter.refreshMeals(id));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart");
    }

    @Override
    protected void setUpComponents() {
        if (App.get(getContext()).getUserComponent() != null) {
            App.get(getContext()).getUserComponent().inject(this);
        }
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void setPresenter(Presenter aPresenter) {
        mPresenter = (MealsListPresenter) aPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mPresenter != null) {
            mPresenter.unbindView(this);
        }
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
        mAdapter.setList(meals);
    }

    @Override
    public void hideRefresh() {
        refreshLayout.setRefreshing(false);
    }
}