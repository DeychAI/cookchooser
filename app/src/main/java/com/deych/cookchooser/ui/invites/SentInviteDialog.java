package com.deych.cookchooser.ui.invites;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.deych.cookchooser.R;

/**
 * Created by deigo on 23.01.2016.
 */
public class SentInviteDialog extends AppCompatDialogFragment {

    private SentInviteDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_sent_invite)
                .setTitle(getActivity().getString(R.string.sent_invite_dialog_title))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    TextView tvSendTo = (TextView) getDialog().findViewById(R.id.etSendTo);
                    if (listener != null) {
                        listener.onPositiveClicked(tvSendTo.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    public void setListener(SentInviteDialogListener listener) {
        this.listener = listener;
    }

    public interface SentInviteDialogListener {
        void onPositiveClicked(String sendTo);
    }
}
