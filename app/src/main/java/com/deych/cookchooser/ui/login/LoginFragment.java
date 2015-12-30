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
import com.deych.cookchooser.ui.base.LfViewState;
import com.deych.cookchooser.ui.meals.MealsActivity;
import com.deych.cookchooser.ui.base.BaseViewStateFragment;
import com.deych.cookchooser.ui.UIScope;
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
    ProgressBar mProgressBar;

    @Bind(R.id.etUsername)
    EditText mEtUsername;

    @Bind(R.id.etPassword)
    EditText mEtPassword;

    @Bind(R.id.btnLogin)
    Button mBtnLogin;

    @OnClick(R.id.btnLogin)
    void loginClick() {
        mPresenter.doLogin(mEtUsername.getText().toString(), mEtPassword.getText().toString());
    }

    @Bind(R.id.link_signup)
    TextView mTvSignUp;

    @OnClick(R.id.link_signup)
    void signUpClick() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new RegisterFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Inject
    LoginPresenter mPresenter;

    @Inject
    LoginViewState mViewState;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getAppComponent().plus(new LoginFragmentModule()).inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void setPresenter(Presenter aPresenter) {
        mPresenter = (LoginPresenter) aPresenter;
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
        mPresenter.bindView(this);
        getUiSubscription().add(RxTextView.editorActionEvents(mEtPassword).subscribe(a -> loginClick()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mPresenter.unbindView(this);
    }

    private void setFormEnabled(boolean aEnabled) {
        mEtUsername.setEnabled(aEnabled);
        mEtPassword.setEnabled(aEnabled);
        mBtnLogin.setEnabled(aEnabled);
        mTvSignUp.setEnabled(aEnabled);
    }

    @Override
    public void showForm() {
        mViewState.setShowForm();
        mProgressBar.setVisibility(View.GONE);
        setFormEnabled(true);
    }

    public void showLoading() {
        mViewState.setShowLoading();
        mEtUsername.setError(null);
        mProgressBar.setVisibility(View.VISIBLE);
        setFormEnabled(false);
    }

    public void showError() {
        showForm();
        mEtPassword.setError(getString(R.string.error_login));
    }

    @Override
    public void loginSuccessful(User user) {
        App.get(getContext()).createUserComponent(user);
        startActivity(new Intent(getContext(), MealsActivity.class));
        getActivity().finish();
    }

    @Override
    public void applyViewState(ViewState aViewState) {
        mViewState.setState(aViewState.getState());
        mViewState.apply(this);
        if (mViewState.getState() == LfViewState.STATE_SHOW_LOADING) {
            mPresenter.checkStateAfterRestore();
        }
    }

    @UIScope
    @Subcomponent(modules = LoginFragmentModule.class)
    public interface LoginFragmentComponent {
        void inject(@NonNull LoginFragment aLoginFragment);
    }

    @Module
    public static class LoginFragmentModule {
        @Provides
        @UIScope
        public ViewStateDelegate provideViewStateDelegate(LoginViewState aViewState) {
            return new ViewStateDelegate(aViewState);
        }
    }
}
