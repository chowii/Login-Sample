package com.examples.loginsample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.examples.loginsample.databinding.ActivityLoginBinding;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    CompositeDisposable mDisposable = new CompositeDisposable();

    public LoginActivity() {
        super();
    }

    LoginViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new LoginViewModel();
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(viewModel);
    }

    private void initViewActionsObservables() {
        mDisposable.add(viewModel.getActionsObservable().subscribe(new Consumer<LoginContract.Actions>() {
            @Override
            public void accept(LoginContract.Actions actions) throws Exception {
                if (actions instanceof LoginContract.Actions.FocusEmail) {
                    activityLoginBinding.emailInputEditText.requestFocus();
                }
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewLiveData(viewModel);
        initViewActionsObservables();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisposable.clear();
    }

    private void initViewLiveData(final LoginViewModel viewModel) {
        LiveData<LoginContract.ViewState> viewState = viewModel.getViewState();
        if (viewState != null) {
            viewState.observe(this, new Observer<LoginContract.ViewState>() {
                @Override
                public void onChanged(LoginContract.ViewState viewState) {
                    if (viewState.getEmailState() == LoginContract.EmailInputState.VALID_EMAIL) {
                        activityLoginBinding.emailInputLayout.setErrorEnabled(false);
                    } else {
                        activityLoginBinding.emailInputLayout.setErrorEnabled(true);
                        activityLoginBinding.emailInputLayout.setError(getString(viewState.getEmailState().getMessageRes()));
                    }

                    if (viewState.getPasswordState() == LoginContract.PasswordInputState.VALID_PASSWORD) {
                        activityLoginBinding.passwordInputLayout.setErrorEnabled(false);
                    } else {
                        activityLoginBinding.passwordInputLayout.setErrorEnabled(true);
                        activityLoginBinding.passwordInputLayout.setError(getString(viewState.getPasswordState().getMessageRes()));
                    }
                    activityLoginBinding.loginButton.setEnabled(viewState.isLoginButtonEnabled());
                    activityLoginBinding.loadingLayout.setVisibility(viewState.isLoading() ? View.VISIBLE : View.GONE);

                }
            });
        }
    }
}
