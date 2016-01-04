package com.deych.cookchooser.ui.group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.R;
import com.deych.cookchooser.ui.base.MainActivityUiDelegate;

/**
 * Created by deigo on 30.12.2015.
 */
public class GroupFragment extends Fragment {

    private MainActivityUiDelegate mainActivityUiDelegate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityUiDelegate = new MainActivityUiDelegate.Builder(context).setToolbarTitle(R.string.title_group)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivityUiDelegate.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivityUiDelegate.onDestroyView();
    }
}