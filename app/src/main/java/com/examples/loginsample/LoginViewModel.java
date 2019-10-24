package com.examples.loginsample;

import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel extends ViewModel {

    private String emailText;
    private String passwordText;
    private MutableLiveData<LoginContract.ViewState> mMutableLiveData = new MutableLiveData<>();
    private PublishSubject<LoginContract.Actions> mActionsSubject = PublishSubject.create();

    Observable<LoginContract.Actions> getActionsObservable() {
        return mActionsSubject;
    }

    LiveData getViewState() {
        return (LiveData) mMutableLiveData;
    }

    public void onEmailChange(String email) {
        emailText = email;
        updateViewState(false, LoginContract.EmailInputState.VALID_EMAIL, null, isLoginDetailsValid());
    }

    public void onPasswordChange(String password) {
        passwordText = password;
        updateViewState(false, null, LoginContract.PasswordInputState.VALID_PASSWORD, isLoginDetailsValid());
    }

    public void isEmailFocused(boolean hasFocus) {
        if (hasFocus) {
            updateViewState(false, LoginContract.EmailInputState.VALID_EMAIL, null, isLoginDetailsValid());
        } else {
            validateEmail();
        }
    }

    private void validateEmail() {
        if (FormValidator.isNullOrEmpty(emailText)) {
            updateViewState(false, LoginContract.EmailInputState.REQUIRED_EMAIL, null, isLoginDetailsValid());
        } else if (!FormValidator.isValidEmail(emailText)) {
            updateViewState(false, LoginContract.EmailInputState.INVALID_EMAIL, null, isLoginDetailsValid());
        }
    }

    public void isPasswordFocused(boolean hasFocus) {
        if (hasFocus) {
            updateViewState(false, null, LoginContract.PasswordInputState.VALID_PASSWORD, isLoginDetailsValid());
        } else {
            validatePassword();
        }
    }

    private void validatePassword() {
        if (!FormValidator.isValidLoginPassword(passwordText)) {
            updateViewState(false, null, LoginContract.PasswordInputState.INVALID_PASSWORD, isLoginDetailsValid());
        }
    }

    public void onLoginClicked() {
        if (!isLoginDetailsValid()) {
            validateEmail();
            validatePassword();
            if (isEmailValid()) {
                mActionsSubject.onNext(new LoginContract.Actions.FocusEmail());
            }
        } else {
            login();
        }
    }

    private void login() {
        hideLoading();
        updateViewState(
                true,
                LoginContract.EmailInputState.VALID_EMAIL,
                LoginContract.PasswordInputState.VALID_PASSWORD,
                isLoginDetailsValid());
    }

    /**
     * This hides the loading view only for testing purposes
     */
    private void hideLoading() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        updateViewState(
                                false,
                                LoginContract.EmailInputState.VALID_EMAIL,
                                LoginContract.PasswordInputState.VALID_PASSWORD,
                                isLoginDetailsValid());
                    }
                },
                1500
        );
    }

    private void updateViewState(
            boolean newIsLoading,
            @Nullable LoginContract.EmailInputState newEmailState,
            @Nullable LoginContract.PasswordInputState newPasswordState,
            boolean newIsLoginDetailsValid
    ) {
        LoginContract.ViewState value = mMutableLiveData.getValue();
        LoginContract.EmailInputState updateEmail;
        if (newEmailState == null) {
            updateEmail = value == null || value.getEmailState() == null ?
                    LoginContract.EmailInputState.VALID_EMAIL : value.getEmailState();
        } else {
            updateEmail = newEmailState;
        }
        LoginContract.PasswordInputState updatePassword;
        if (newPasswordState == null) {
            updatePassword = value == null || value.getPasswordState() == null ?
                    LoginContract.PasswordInputState.VALID_PASSWORD : value.getPasswordState();
        } else {
            updatePassword = newPasswordState;
        }
        mMutableLiveData.setValue(new LoginContract.ViewState(
                newIsLoading,
                updateEmail,
                updatePassword,
                newIsLoginDetailsValid));
    }

    private boolean isEmailValid() {
        return FormValidator.isValidEmail(emailText);
    }

    private boolean isLoginDetailsValid() {
        return FormValidator.isValidEmail(emailText)
                && FormValidator.isValidLoginPassword(passwordText);
    }

}
