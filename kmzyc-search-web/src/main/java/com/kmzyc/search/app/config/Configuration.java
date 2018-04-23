package com.kmzyc.search.app.config;

public class Configuration {

    public static String getString(String key) {

        return com.kmzyc.search.config.Configuration.getString(key);
    }

    /**
     * 根据key回去value
     * 
     * @param name
     * @return
     */
    public static String getContextProperty(String name) {

        return getString(name);
    }

    public static int getInt(String name, int defaultValue) {

        return com.kmzyc.search.config.Configuration.getInt(name, defaultValue);
    }
}
