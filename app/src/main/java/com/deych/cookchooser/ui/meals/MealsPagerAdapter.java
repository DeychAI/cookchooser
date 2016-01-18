package com.deych.cookchooser.ui.meals;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deych.cookchooser.db.entities.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsPagerAdapter extends FragmentPagerAdapter {

    private List<Category> categories;

    public MealsPagerAdapter(FragmentManager fm) {
        super(fm);
        categories = new ArrayList<>();
    }

    public Fragment getItem(int position) {
        return MealsListFragment.newInstance(categories.get(position).getId());
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
    }

    public Category getCategory(int id) {
        return categories.get(id);
    }
}
