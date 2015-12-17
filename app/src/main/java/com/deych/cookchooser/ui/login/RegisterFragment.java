package com.deych.cookchooser.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.deych.cookchooser.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deigo on 16.12.2015.
 */
public class RegisterFragment extends Fragment {
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.etUsername)
    EditText mEtUsername;

    @Bind(R.id.etPassword)
    EditText mEtPassword;

    @Bind(R.id.btnLogin)
    Button mBtnLogin;

    @OnClick(R.id.btnLogin)
    void loginClick() {
//        presenter.doLogin(mEtUsername.getText().toString(), mEtPassword.getText().toString());
    }

    private boolean isDestroyedBySystem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onResume() {
        super.onResume();
        isDestroyedBySystem = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("@Register", "onSaveInstanceState");
        isDestroyedBySystem = true;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        Log.v("@Register", "onDestroy. isDestroyedBySystem = " + isDestroyedBySystem);
        if (!isDestroyedBySystem) {

        }
    }
}
