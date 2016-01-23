package com.deych.cookchooser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
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
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.shared_pref.Preferences;
import com.deych.cookchooser.ui.base.ui_controls.MainUi;
import com.deych.cookchooser.ui.group.GroupFragment;
import com.deych.cookchooser.ui.invites.InvitesFragment;
import com.deych.cookchooser.ui.login.LoginActivity;
import com.deych.cookchooser.ui.meals.MealsHostFragment;
import com.deych.cookchooser.ui.meals.MealsListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MainActivityView,
        NavigationView.OnNavigationItemSelectedListener,
        MainUi {

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

    public FloatingActionButton fab() {
        return fab;
    }

    public TabLayout tabs() {
        return tabs;
    }

    public Toolbar toolbar() {
        return toolbar;
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

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_list);

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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                presenter.colorSelected(MealColor.None);
                break;
            case R.id.nav_list_red:
                showMealsHostFragment();
                presenter.colorSelected(MealColor.Red);
                break;
            case R.id.nav_list_green:
                showMealsHostFragment();
                presenter.colorSelected(MealColor.Green);
                break;
            case R.id.nav_list_blue:
                showMealsHostFragment();
                presenter.colorSelected(MealColor.Blue);
                break;
            case R.id.nav_list_orange:
                showMealsHostFragment();
                presenter.colorSelected(MealColor.Orange);
                break;
            case R.id.nav_group:
                showGroupFragment();
                break;
            case R.id.nav_invites:
                showInvitesFragment();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInvitesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new InvitesFragment())
                .commit();
    }

    private void showGroupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new GroupFragment())
                .commit();
    }

    private void showMealsHostFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.content) instanceof MealsHostFragment) {
            return;
        }
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

    @Override
    public void onColorSelected() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MealsListFragment.ACTION_FILTER_MEALS));
    }

    @Override
    public void updateColorCount(MealColor color, int count) {
        setColorActionView(color, count);
    }

    @Override
    public void selectColor(MealColor selectedColor) {
        navigationView.setCheckedItem(selectedColor.menuRes());
    }

    private void setColorActionView(MealColor color, int count) {
        MenuItem item = navigationView.getMenu().findItem(color.menuRes());
        View view = MenuItemCompat.getActionView(item);
        if (view == null) {
            view = View.inflate(this, R.layout.drawer_action_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.colorSelector);
            imageView.setImageResource(color.drawableRes());
        }
        TextView textView = (TextView) view.findViewById(R.id.colorSelectorText);
        textView.setText(String.valueOf(count));
        MenuItemCompat.setActionView(item, view);
    }
}
