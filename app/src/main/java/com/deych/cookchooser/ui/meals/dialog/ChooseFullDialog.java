package com.deych.cookchooser.ui.meals.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.deych.cookchooser.R;

/**
 * Created by deigo on 23.01.2016.
 */
public class ChooseFullDialog extends AppCompatDialogFragment {

    private static final String ARG_MESSAGE = "arg.message";

    public static ChooseFullDialog newInstance(String message) {
        ChooseFullDialog dialog = new ChooseFullDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_full_menu_title)
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }


}
