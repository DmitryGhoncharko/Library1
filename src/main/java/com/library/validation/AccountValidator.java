package com.library.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator {

    private AccountValidator() {
    }

    public static AccountValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountValidator INSTANCE = new AccountValidator();
    }

    public boolean validate(String login, String password) {
        if (login == null || password == null || login.equals(password)) {
            return false;
        }
        Pattern patternLogin = Pattern.compile(".{3,30}");
        Matcher matcherLogin = patternLogin.matcher(login);

        Pattern patternPassword = Pattern.compile(".{3,100}");
        Matcher matcherPassword = patternPassword.matcher(password);

        return matcherLogin.matches() && matcherPassword.matches();
    }
}
