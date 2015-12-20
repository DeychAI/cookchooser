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

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public MealsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mList.add(fragment);
        mTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
