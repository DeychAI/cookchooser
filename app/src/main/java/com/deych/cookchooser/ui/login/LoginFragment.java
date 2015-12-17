package com.deych.cookchooser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.api.ServiceFactory;
import com.deych.cookchooser.ui.MainActivity;
import com.deych.cookchooser.ui.base.BaseViewStateFragment;
import com.deych.cookchooser.ui.base.FragmentScope;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.ViewState;
import com.deych.cookchooser.ui.base.ViewStateDelegate;

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
public class LoginFragment extends BaseViewStateFragment implements LoginView{

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
//        getFragmentManager().beginTransaction()
//                .add(R.id.content, new RegisterFragment())
//                .addToBackStack(null)
//                .commit();

    }

    @Inject
    LoginPresenter mPresenter;

    @Inject
    LoginViewState mViewState;


    @Override
    protected void setUpComponents() {
        App.get(getContext()).getAppComponent().plus(new LoginFragmentModule()).inject(this);
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
    public void showLoginForm() {
        mViewState.setShowLoginForm();
        mProgressBar.setVisibility(View.GONE);
        setFormEnabled(true);
    }

    public void showLoading() {
        mViewState.setShowLoading();
        mProgressBar.setVisibility(View.VISIBLE);
        setFormEnabled(false);
    }

    public void showError() {
        mViewState.setShowLoginForm();
        mProgressBar.setVisibility(View.GONE);
        setFormEnabled(true);
        mEtUsername.setError(getString(R.string.error_login));
    }

    @Override
    public void loginSuccessful() {
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public Presenter onEmptyCache() {
        return mPresenter;
    }

    @Override
    public void restoredFromCache(Presenter aPresenter) {
        mPresenter = (LoginPresenter) aPresenter;
    }

    @Override
    public void applyViewState(ViewState aViewState) {
        mViewState = (LoginViewState) aViewState;
        mViewState.apply(this);
    }

    @FragmentScope
    @Subcomponent(modules = LoginFragmentModule.class)
    public interface LoginFragmentComponent {
        void inject(@NonNull LoginFragment aLoginFragment);
    }

    @Module
    public static class LoginFragmentModule {

        @Provides
        @FragmentScope
        public LoginPresenter provideLoginPresenter(ServiceFactory aServiceFactory) {
            return new LoginPresenter(aServiceFactory);
        }

        @Provides
        @FragmentScope
        public LoginViewState provideLoginViewState() {
            return new LoginViewState();
        }

        @Provides
        @FragmentScope
        public ViewStateDelegate provideViewStateDelegate(LoginViewState aViewState) {
            return new ViewStateDelegate(aViewState);
        }
    }
}
