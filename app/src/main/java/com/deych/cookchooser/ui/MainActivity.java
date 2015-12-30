package com.deych.cookchooser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.ui.group.GroupFragment;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.meals.MealsHostFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MainActivityView, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    TextView tvUsername;

    TextView tvName;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    public FloatingActionButton getFab() {
        return fab;
    }

    public TabLayout getTabs() {
        return tabs;
    }

    @Inject
    MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.get(this).getUserComponent().inject(this);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_list);
        tvUsername = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvUsername);
        tvName = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvName);
        mPresenter.bindView(this);

        if (savedInstanceState == null) {
            showMealsHostFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mPresenter.logout();
                break;
            case R.id.nav_list:
                showMealsHostFragment();
                break;
            case R.id.nav_group:
                showGroupFragment();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showGroupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new GroupFragment())
                .commit();
    }

    private void showMealsHostFragment() {
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, new MealsHostFragment())
                    .commit();
    }

    @Override
    public void showLoginScreen() {
        App.get(this).releaseUserComponent();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void bindUserData(String username, String name) {
        tvName.setText(name);
        tvUsername.setText(username);
    }
}
