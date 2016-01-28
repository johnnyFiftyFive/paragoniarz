package org.j55.paragoniarz.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author johnnyFiftyFive
 */
public class Properties {
    public static final String USER = "paragoniarz.user";
    public static final String PASS = "paragoniarz.pass";
    public static final String PHONE = "paragoniarz.phone";

    private static final java.util.Properties properties;

    static {
        InputStream is = Properties.class.getResourceAsStream("/app.properties");
        properties = new java.util.Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cant load properties file");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
