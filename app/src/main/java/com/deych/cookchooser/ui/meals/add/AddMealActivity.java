package com.deych.cookchooser.ui.meals.add;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.deych.cookchooser.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.deych.cookchooser.ui.base.ui_controls.FabToolbarUi;
import com.deych.cookchooser.ui.meals.detail.MealsDetailFragment;

/**
 * Created by deigo on 04.01.2016.
 */
public class AddMealActivity extends AppCompatActivity implements FabToolbarUi{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, new MealsDetailFragment())
                    .commit();
        }
    }

    @Override
    public FloatingActionButton fab(){
        return fab;
    }

    @Override
    public Toolbar toolbar(){
        return toolbar;
    }
}
