package com.library.util;

import java.util.ResourceBundle;

public class ConfigurationManager {

    private static final ResourceBundle resourceBundler = ResourceBundle.getBundle("config");

    private ConfigurationManager() {
    }

    public static String getProperty(String key) {
        return resourceBundler.getString(key);
    }


}
