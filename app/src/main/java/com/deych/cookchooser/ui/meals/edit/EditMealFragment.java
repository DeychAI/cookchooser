package com.deych.cookchooser.ui.meals.edit;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Category;
import com.deych.cookchooser.db.entities.Meal;
import com.deych.cookchooser.db.entities.MealColor;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.ui_controls.FabToolbarUiDelegate;
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

    private FabToolbarUiDelegate fabToolbarUiDelegate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meals_edit, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bindView(this);
        presenter.bindData(getArguments().getString(ARG_MEAL_UUID), getArguments().getLong(ARG_CATEGORY_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fabToolbarUiDelegate.onDestroyView();
        presenter.unbindView(this);
        ButterKnife.unbind(this);
    }

    @Override
    public void setCategories(List<Category> categories, long selectedCategory) {
        spCategory.setItemsArray(categories);
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == selectedCategory) {
                spCategory.setSelection(i, false);
                return;
            }
        }
        throw new IllegalStateException("Can't find category!");
    }

    private void save() {
        Category category = (Category) spCategory.getSpinner().getAdapter()
                .getItem(spCategory.getSpinner().getSelectedItemPosition());

        presenter.save(etName.getText().toString(), category.getId(),
                MealColor.fromRadioBtn(colorGroup.getCheckedRadioButtonId()));
    }

    @Override
    public void onSaved() {
        getActivity().finish();
    }

    @Override
    public void showError() {
        fabToolbarUiDelegate.createSnackbar(R.string.error_save_meal, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setMealData(Meal meal) {
        etName.setText(meal.getName());
        colorGroup.check(meal.getColor().radioBtnRes());
        String title;
        if (TextUtils.isEmpty(meal.getName())) {
            title = getString(R.string.title_add_meal);
        } else {
            title = meal.getName();
        }
        if (fabToolbarUiDelegate == null) {
            fabToolbarUiDelegate = new FabToolbarUiDelegate.Builder(getActivity())
                    .setToolbarTitle(title)
                    .showFab()
                    .setFabDrawable(R.drawable.ic_done)
                    .setFabListener(v -> save())
                    .build();
        }
        fabToolbarUiDelegate.onViewCreated();

    }

}
