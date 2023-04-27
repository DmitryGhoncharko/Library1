package com.library.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookValidator {

    private BookValidator() {
    }

    public static BookValidator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final BookValidator INSTANCE = new BookValidator();
    }

    public boolean validate(String title, String date, Integer amountOfLeft) {
        if (title == null || date == null || amountOfLeft == null || amountOfLeft.equals(0)) {
            return false;
        }

        Pattern patternTitle = Pattern.compile(".{0,100}");
        Matcher matcherTitle = patternTitle.matcher(title);

        Pattern patternDate = Pattern.compile("^\\d{4}[-]?((((0[13578])|(1[02]))[-]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[-]?(([0-2][0-9])|(30)))|(02[-]?[0-2][0-9]))$");
        Matcher matcherDate = patternDate.matcher(date);


        Pattern patternNumber = Pattern.compile("^\\d{1,4}");
        Matcher matcherAmountOfLeft = patternNumber.matcher(amountOfLeft.toString());

        return matcherTitle.matches() && matcherDate.matches() && matcherAmountOfLeft.matches();
    }
}
