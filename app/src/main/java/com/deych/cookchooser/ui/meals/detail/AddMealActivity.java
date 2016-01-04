package com.deych.cookchooser.ui.meals.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.deych.cookchooser.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 04.01.2016.
 */
public class AddMealActivity extends AppCompatActivity{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, new MealsDetailFragment())
                    .commit();
        }
    }
}
