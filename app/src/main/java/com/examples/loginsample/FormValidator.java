/*
 * Copyright (c) 2019 Woolworths. All rights reserved.
 */

package com.examples.loginsample;

import android.util.Patterns;

public class FormValidator {

    public static boolean isValidEmail(String emailId) {
        return !isNullOrWhiteSpace(emailId)
                && Patterns.EMAIL_ADDRESS.matcher(emailId.trim()).matches();
    }

    public static boolean isValidLoginPassword(String password) {
        return !isNullOrEmpty(password);
    }

    public static boolean isNullOrWhiteSpace(String string) {
        return (string == null || string.length() == 0 || string.trim().length() == 0);
    }

    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.length() == 0);
    }

}
