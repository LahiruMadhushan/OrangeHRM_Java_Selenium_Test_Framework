package com.orangehrm.automation.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads login test data from testdata/login-data.properties so credentials
 * never appear hardcoded inside test classes.
 */
public final class TestDataReader {

    private static final String DATA_FILE = "testdata/login-data.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = TestDataReader.class.getClassLoader().getResourceAsStream(DATA_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Unable to find " + DATA_FILE + " on the classpath");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + DATA_FILE, e);
        }
    }

    private TestDataReader() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing test data value for key: " + key);
        }
        return value;
    }

    public static String validUsername() {
        return get("valid.username");
    }

    public static String validPassword() {
        return get("valid.password");
    }

    public static String invalidUsername() {
        return get("invalid.username");
    }

    public static String invalidPassword() {
        return get("invalid.password");
    }
}
