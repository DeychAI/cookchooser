package com.deych.cookchooser.ui.meals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.api.service.MealsService;
import com.deych.cookchooser.models.MealsModel;

import java.util.ArrayList;

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
public class MealsListFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView list;

    @Inject
    MealsModel mMealsModel;

    @Inject
    Retrofit mRetrofit;

    private Subscription mSubscription;
    private MealsAdapter mAdapter;

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
        App.get(getContext()).getUserComponent().inject(this);

        id = getArguments().getLong("id");

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MealsAdapter();


        list.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
                mSubscription = mMealsModel.getMeals(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    mAdapter.setList(l);
                }, e -> {
                    Timber.v("Error " + e);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mSubscription.unsubscribe();
    }
}