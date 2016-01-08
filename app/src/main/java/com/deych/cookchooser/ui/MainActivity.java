package com.deych.cookchooser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return actionBarDrawerToggle;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public TabLayout getTabs() {
        return tabs;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.get(this).getUserComponent().inject(this);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.title_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> onBackPressed());
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_list);

        MenuItem item = navigationView.getMenu().findItem(R.id.nav_list);
        View view = View.inflate(this, R.layout.drawer_action_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.colorSelector);
        imageView.setImageResource(R.drawable.none_btn);
        MenuItemCompat.setActionView(item, view);

        item = navigationView.getMenu().findItem(R.id.nav_list_red);
        view = View.inflate(this, R.layout.drawer_action_layout, null);
        imageView = (ImageView) view.findViewById(R.id.colorSelector);
        imageView.setImageResource(R.drawable.red_btn);
        MenuItemCompat.setActionView(item, view);


        item = navigationView.getMenu().findItem(R.id.nav_list_green);
        view = View.inflate(this, R.layout.drawer_action_layout, null);
        imageView = (ImageView) view.findViewById(R.id.colorSelector);
        imageView.setImageResource(R.drawable.green_btn);
        MenuItemCompat.setActionView(item, view);

        item = navigationView.getMenu().findItem(R.id.nav_list_blue);
        view = View.inflate(this, R.layout.drawer_action_layout, null);
        imageView = (ImageView) view.findViewById(R.id.colorSelector);
        imageView.setImageResource(R.drawable.blue_btn);
        MenuItemCompat.setActionView(item, view);

        item = navigationView.getMenu().findItem(R.id.nav_list_orange);
        view = View.inflate(this, R.layout.drawer_action_layout, null);
        imageView = (ImageView) view.findViewById(R.id.colorSelector);
        imageView.setImageResource(R.drawable.orange_btn);
        MenuItemCompat.setActionView(item, view);

        tvUsername = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvUsername);
        tvName = ButterKnife.findById(navigationView.getHeaderView(0), R.id.tvName);
        presenter.bindView(this);

        if (savedInstanceState == null) {
            showMealsHostFragment();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                presenter.logout();
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
