package com.deych.cookchooser.ui.meals.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.uicontrols.ToolbarUiDelegate;
import com.farbod.labelledspinner.LabelledSpinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 18.01.2016.
 */
public class EditMealFragment extends BaseFragment implements EditMealView{

    private static final String ARG_CATEGORY_ID = "arg.category.id";
    private static final String ARG_MEAL_UUID = "arg.meal.uuid";

    private static final String CURRENT_CAT_POSITION = "bundle.extra.position";

    public static EditMealFragment newInstance(long categoryId) {
        EditMealFragment fragment = new EditMealFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditMealFragment newInstance(String uuid) {
        EditMealFragment fragment = new EditMealFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEAL_UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    EditMealPresenter presenter;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getUserComponent().inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void setPresenter(Presenter presenter) {
        this.presenter = (EditMealPresenter) presenter;
    }

    @Bind(R.id.colorGroup)
    RadioGroup colorGroup;

    @Bind(R.id.spCategory)
    LabelledSpinner spCategory;

    @Bind(R.id.etName)
    EditText etName;

    @Bind(R.id.tilName)
    TextInputLayout tilName;

    @Bind(R.id.etDescription)
    EditText etDescription;

    private ToolbarUiDelegate toolbarUiDelegate;

    private int spCategoryPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_meal, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            spCategoryPosition = savedInstanceState.getInt(CURRENT_CAT_POSITION, -1);
        }
        presenter.bindView(this);
        presenter.bindData(getArguments().getString(ARG_MEAL_UUID), getArguments().getLong(ARG_CATEGORY_ID));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_meals, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toolbarUiDelegate != null) {
            toolbarUiDelegate.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (spCategory != null) {
            outState.putInt(CURRENT_CAT_POSITION, spCategory.getSpinner().getSelectedItemPosition());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(this);
        toolbarUiDelegate.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCategories(List<Category> categories, long selectedCategory) {
        spCategory.setItemsArray(categories);
        if (spCategoryPosition > -1) {
            spCategory.setSelection(spCategoryPosition, false);
            return;
        }
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == selectedCategory) {
                spCategory.setSelection(i, false);
                return;
            }
        }
        throw new IllegalStateException("Can't find category!");
    }

    @Override
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.title_add_meal);
        }
        if (toolbarUiDelegate == null) {
            toolbarUiDelegate = new ToolbarUiDelegate.Builder(getActivity())
                    .setToolbarTitle(title)
                    .build();
        }
        toolbarUiDelegate.onViewCreated();
    }

    private void save() {
        Category category = (Category) spCategory.getSpinner().getAdapter()
                .getItem(spCategory.getSpinner().getSelectedItemPosition());

        presenter.save(etName.getText().toString(),
                category.getId(),
                MealColor.fromRadioBtn(colorGroup.getCheckedRadioButtonId()),
                etDescription.getText().toString());
    }

    @Override
    public void onSaved() {
        getActivity().finish();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error_save_meal, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMealData(Meal meal) {
        tilName.setHintAnimationEnabled(false);
        etName.setText(meal.getName());
        etName.setSelection(etName.getText().length());
        etDescription.setText(meal.getDescription());
        colorGroup.check(meal.getColor().radioBtnRes());
    }

}
