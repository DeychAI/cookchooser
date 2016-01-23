package com.deych.cookchooser.ui.invites;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.deych.cookchooser.R;

/**
 * Created by deigo on 23.01.2016.
 */
public class AcceptInviteDialog extends AppCompatDialogFragment {

    private static final String ARG_FROM = "arg.from";

    public static AcceptInviteDialog newInstance(String from) {
        AcceptInviteDialog dialog = new AcceptInviteDialog();
        Bundle args = new Bundle();
        args.putString(ARG_FROM, from);
        dialog.setArguments(args);
        return dialog;
    }

    private AcceptInviteDialogListener listener;

    public void setListener(AcceptInviteDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(String.format(getActivity().getString(R.string.dialog_accept_invite_message),
                getArguments().getString(ARG_FROM)))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (listener != null) {
                        listener.onPositiveClicked();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    public interface AcceptInviteDialogListener {
        void onPositiveClicked();
    }
}
