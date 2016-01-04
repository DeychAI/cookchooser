package com.deych.cookchooser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.base.LfViewState;
import com.deych.cookchooser.ui.base.BaseViewStateFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.ViewState;
import com.deych.cookchooser.ui.base.ViewStateDelegate;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by deigo on 16.12.2015.
 */
public class LoginFragment extends BaseViewStateFragment implements LoginView {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.etUsername)
    EditText etUsername;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @OnClick(R.id.btnLogin)
    void loginClick() {
        presenter.doLogin(etUsername.getText().toString(), etPassword.getText().toString());
    }

    @Bind(R.id.link_signup)
    TextView tvSignUp;

    @OnClick(R.id.link_signup)
    void signUpClick() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new RegisterFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Inject
    LoginPresenter presenter;

    @Inject
    LoginViewState viewState;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getAppComponent().plus(new LoginFragmentModule()).inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void setPresenter(Presenter presenter) {
        this.presenter = (LoginPresenter) presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bindView(this);
        getUiSubscription().add(RxTextView.editorActionEvents(etPassword).subscribe(a -> loginClick()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.unbindView(this);
    }

    private void setFormEnabled(boolean enabled) {
        etUsername.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        btnLogin.setEnabled(enabled);
        tvSignUp.setEnabled(enabled);
    }

    @Override
    public void showForm() {
        viewState.setShowForm();
        progressBar.setVisibility(View.GONE);
        setFormEnabled(true);
    }

    public void showLoading() {
        viewState.setShowLoading();
        etUsername.setError(null);
        progressBar.setVisibility(View.VISIBLE);
        setFormEnabled(false);
    }

    public void showError() {
        showForm();
        etPassword.setError(getString(R.string.error_login));
    }

    @Override
    public void loginSuccessful(User user) {
        App.get(getContext()).createUserComponent(user);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void applyViewState(ViewState viewState) {
        this.viewState.setState(viewState.getState());
        this.viewState.apply(this);
        if (this.viewState.getState() == LfViewState.STATE_SHOW_LOADING) {
            presenter.checkStateAfterRestore();
        }
    }

    @LoginScope
    @Subcomponent(modules = LoginFragmentModule.class)
    public interface LoginFragmentComponent {
        void inject(@NonNull LoginFragment aLoginFragment);
    }

    @Module
    public static class LoginFragmentModule {
        @Provides
        @LoginScope
        public ViewStateDelegate provideViewStateDelegate(LoginViewState aViewState) {
            return new ViewStateDelegate(aViewState);
        }
    }
}
