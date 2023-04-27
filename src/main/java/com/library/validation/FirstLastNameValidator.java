package com.library.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstLastNameValidator {

    private FirstLastNameValidator() {
    }

    public static FirstLastNameValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final FirstLastNameValidator INSTANCE = new FirstLastNameValidator();
    }

    public boolean validate(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[A-Za-z]{1,20}");
        Matcher matcherFirstName = pattern.matcher(firstName);
        Pattern patternProtectInjection = Pattern.compile("[\\<]");
        Matcher matcherProtectInjection = patternProtectInjection.matcher(firstName);
        firstName = matcherProtectInjection.replaceAll("%3c");
        Matcher matcherLastName = pattern.matcher(lastName);

        return matcherFirstName.matches() && matcherLastName.matches();
    }
}
