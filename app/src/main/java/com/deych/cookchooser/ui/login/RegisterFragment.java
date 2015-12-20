package com.deych.cookchooser.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.ui.UIScope;
import com.deych.cookchooser.ui.base.BaseViewStateFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.ViewState;
import com.deych.cookchooser.ui.base.ViewStateDelegate;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by deigo on 16.12.2015.
 */
public class RegisterFragment extends BaseViewStateFragment implements RegisterView{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.etUsername)
    EditText etUsername;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.etRepeatPassword)
    EditText etRepeatPassword;

    @Bind(R.id.etName)
    EditText etName;

    @Bind(R.id.btnRegister)
    Button btnRegister;

    @OnClick(R.id.btnRegister)
    void registerClick() {
        mPresenter.doRegister(etUsername.getText().toString(),
                etPassword.getText().toString(),
                etRepeatPassword.getText().toString(),
                etName.getText().toString());
    }

    @Inject
    RegisterPresenter mPresenter;

    @Inject
    RegisterViewState mViewState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.bindView(this);
        getUiSubscription().add(RxTextView.editorActionEvents(etName).subscribe(a -> registerClick()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mPresenter.unbindView(this);
    }

    private void setFormEnabled(boolean aEnabled) {
        etUsername.setEnabled(aEnabled);
        etPassword.setEnabled(aEnabled);
        etRepeatPassword.setEnabled(aEnabled);
        etName.setEnabled(aEnabled);
        btnRegister.setEnabled(aEnabled);
    }

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getAppComponent().plus(new RegisterFragmentModule()).inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void setPresenter(Presenter aPresenter) {
        mPresenter = (RegisterPresenter) aPresenter;
    }

    @Override
    public void showForm() {
        mViewState.setShowForm();
        setFormEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mViewState.setShowLoading();
        etUsername.setError(null);
        etPassword.setError(null);
        etRepeatPassword.setError(null);
        setFormEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmailNotValidError() {
        showForm();
        etUsername.setError("Укажите корректный Email");
    }

    @Override
    public void showUserExistsError() {
        showForm();
        etUsername.setError("Пользователь с таким именем уже существует");
    }

    @Override
    public void showPasswordBlankError() {
        showForm();
        etPassword.setError("Пароль не может быть пустым");
    }

    @Override
    public void showPasswordMustMatchError() {
        showForm();
        etRepeatPassword.setError("Пароли должны совпадать");
    }

    @Override
    public void showError() {
        showForm();
        Snackbar.make(btnRegister, "Ошибка. Попробуйте позднее", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void registerSuccessful() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void applyViewState(ViewState aViewState) {
        mViewState = (RegisterViewState) aViewState;
        mViewState.apply(this);
    }

    @UIScope
    @Subcomponent(modules = RegisterFragmentModule.class)
    public interface RegisterFragmentComponent {
        void inject(@NonNull RegisterFragment aFragment);
    }

    @dagger.Module
    public static class RegisterFragmentModule {
        @Provides
        @UIScope
        public ViewStateDelegate provideViewStateDelegate(RegisterViewState aViewState) {
            return new ViewStateDelegate(aViewState);
        }
    }

}
