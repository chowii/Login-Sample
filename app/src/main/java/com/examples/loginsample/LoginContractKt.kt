package com.examples.loginsample

interface LoginContractKt {

    data class ViewState(
            val emailState: EmailInputState,
            val passwordState: PasswordInputState,
            val isLoginButtonEnabled: Boolean)

    interface InputState {
        val messageRes: Int
            get() = 0
    }

    enum class EmailInputState : InputState {
        INVALID_EMAIL {
            override val messageRes = R.string.invalid_email_error_text
        },

        REQUIRED_EMAIL {
            override val messageRes = R.string.empty_email_error
        },
        VALID_EMAIL {
            override val messageRes = 0
        }
    }

    enum class PasswordInputState : InputState {
        INVALID_PASSWORD {
            override val messageRes = R.string.invalid_password_error_text
        },

        REQUIRED_PASSWORD {
            override val messageRes = R.string.empty_password_error
        },
        VALID_PASSWORD {
            override val messageRes = 0
        }
    }

}