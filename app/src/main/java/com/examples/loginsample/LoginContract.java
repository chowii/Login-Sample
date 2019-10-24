package com.examples.loginsample;

import org.jetbrains.annotations.NotNull;


public interface LoginContract {
    final class ViewState {
        @NotNull
        private final LoginContract.EmailInputState emailState;
        @NotNull
        private final LoginContract.PasswordInputState passwordState;

        private final boolean isLoginButtonEnabled;
        private final boolean isLoading;

        boolean isLoading() {
            return isLoading;
        }

        @NotNull
        final LoginContract.EmailInputState getEmailState() {
            return this.emailState;
        }

        @NotNull
        final LoginContract.PasswordInputState getPasswordState() {
            return this.passwordState;
        }

        final boolean isLoginButtonEnabled() {
            return this.isLoginButtonEnabled;
        }

        ViewState(
                boolean isLoading,
                @NotNull LoginContract.EmailInputState emailState,
                @NotNull LoginContract.PasswordInputState passwordState,
                boolean isLoginButtonEnabled
        ) {
            this.isLoading = isLoading;
            this.emailState = emailState;
            this.passwordState = passwordState;
            this.isLoginButtonEnabled = isLoginButtonEnabled;
        }
    }

    class Actions {
        static class FocusEmail extends Actions {
        }
    }

    interface InputState {
        int getMessageRes();
    }

    enum EmailInputState implements LoginContract.InputState {
        INVALID_EMAIL {
            @Override
            public int getMessageRes() {
                return R.string.invalid_email_error_text;
            }
        },
        REQUIRED_EMAIL {
            @Override
            public int getMessageRes() {
                return R.string.empty_email_error;
            }
        },
        VALID_EMAIL {
            @Override
            public int getMessageRes() {
                return 0;
            }
        }

    }

    enum PasswordInputState implements LoginContract.InputState {
        INVALID_PASSWORD {
            @Override
            public int getMessageRes() {
                return R.string.empty_password_error;
            }
        },
        VALID_PASSWORD {
            @Override
            public int getMessageRes() {
                return 0;
            }
        };

    }
}
