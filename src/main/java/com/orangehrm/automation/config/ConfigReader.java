package com.orangehrm.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration from config.properties (classpath) once,
 * with the option to override any value via a matching JVM system property
 * (e.g. -Dbrowser=firefox), which is useful for CI/Jenkins runs.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE + " on the classpath");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Missing config value for key: " + key);
        }
        return value.trim();
    }

    public static String getAppUrl() {
        return get("app.url");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }
}
