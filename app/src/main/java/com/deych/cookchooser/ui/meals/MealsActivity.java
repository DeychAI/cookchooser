package com.deych.cookchooser.ui.meals;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.ui.base.BaseActivity;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.login.LoginActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MealsView {

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    TextView tvUsername;

    TextView tvName;

    private MealsPagerAdapter mMealsAdapter;

    @Inject
    MealsActivityPresenter mPresenter;

    @OnClick(R.id.fab)
    void fabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void setUpComponents() {
        App.get(this).getUserComponent().inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void setPresenter(Presenter aPresenter) {
        mPresenter = (MealsActivityPresenter) aPresenter;
    }

    @Override
    protected void bindViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mMealsAdapter = new MealsPagerAdapter(getSupportFragmentManager());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_list);
        tvUsername = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvUsername);
        tvName = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvName);

        mPresenter.bindView(this);
        mPresenter.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unbindView(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mPresenter.logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showCategories(List<Category> categories) {
        if(mMealsAdapter.getCount() > 0) {
            return;
        }
        for (Category category : categories) {
            mMealsAdapter.addFragment(MealsListFragment.newInstance(category.getId()), category.getName());
            viewPager.setAdapter(mMealsAdapter);
            tabs.setupWithViewPager(viewPager);
        }
        mMealsAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishIfNotAuthorized() {

    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void bindUserData(String username, String name) {
        tvName.setText(name);
        tvUsername.setText(username);
    }
}
