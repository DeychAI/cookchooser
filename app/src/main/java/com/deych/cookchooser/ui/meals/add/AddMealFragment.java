package com.deych.cookchooser.ui.meals.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.R;
import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.base.MainActivityUiDelegate;

/**
 * Created by deigo on 08.01.2016.
 */
public class AddMealFragment extends Fragment {

    private MainActivityUiDelegate mainActivityUiDelegate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meals_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivityUiDelegate = new MainActivityUiDelegate.Builder(getActivity())
                .setToolbarTitle(R.string.title_add_meal)
                .showFab()
                .setFabDrawable(R.drawable.ic_done)
                .setFabListener(v ->
                        Snackbar.make(v, "Adding!", Snackbar.LENGTH_LONG).setAction("Action", null).show())
                .build();

        mainActivityUiDelegate.onViewCreated();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivityUiDelegate.onDestroyView();
    }
}
