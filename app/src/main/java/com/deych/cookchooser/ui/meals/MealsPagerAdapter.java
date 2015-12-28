package com.deych.cookchooser.ui.meals;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.db.entities.Category;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsPagerAdapter extends FragmentPagerAdapter {

    private List<Category> mCategories;

    public MealsPagerAdapter(FragmentManager fm) {
        super(fm);
        mCategories = new ArrayList<>();
    }

    public Fragment getItem(int position) {
        return MealsListFragment.newInstance(mCategories.get(position).getId());
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories.get(position).getName();
    }
}
