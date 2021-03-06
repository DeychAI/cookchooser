package com.deych.cookchooser.ui.meals.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.deych.cookchooser.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.deych.cookchooser.ui.base.config.ToolbarUi;

/**
 * Created by deigo on 04.01.2016.
 */
public class EditMealActivity extends AppCompatActivity implements ToolbarUi{

    private static final String EXTRA_CATEGORY_ID = "extra.category.id";
    private static final String EXTRA_MEAL_UUID = "extra.meal.uuid";

    public static void startForAdding(Context context, long categoryId) {
        Intent intent = new Intent(context, EditMealActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        context.startActivity(intent);
    }

    public static void startForEditing(Context context, String uuid) {
        Intent intent = new Intent(context, EditMealActivity.class);
        intent.putExtra(EXTRA_MEAL_UUID, uuid);
        context.startActivity(intent);
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        ButterKnife.bind(this);

        //With this setTitle, toolbar title will be changed twice. Here and in fragment
        //Without - it will be changed 3 times. Here, in fragment and then in onPostCreate(). sic!
//        toolbar.setTitle(R.string.title_add_meal);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, createFragment())
                    .commit();
        }
    }

    private EditMealFragment createFragment() {
        if (getIntent().hasExtra(EXTRA_MEAL_UUID)) {
            //Editing
            return EditMealFragment.newInstance(getIntent().getStringExtra(EXTRA_MEAL_UUID));
        } else {
            //Adding
            return EditMealFragment.newInstance(getIntent().getLongExtra(EXTRA_CATEGORY_ID, 0));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public Toolbar toolbar(){
        return toolbar;
    }
}
