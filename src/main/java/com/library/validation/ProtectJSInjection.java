package com.library.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtectJSInjection {

    public static ProtectJSInjection getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ProtectJSInjection INSTANCE = new ProtectJSInjection();
    }

    public String protectInjection(String name) {
        if (name == null){
            return null;
        }
        Pattern patternProtectInjection = Pattern.compile("\\<");
        Matcher matcherProtectInjection = patternProtectInjection.matcher(name);
        return matcherProtectInjection.replaceAll("\\\\<");
    }
}
