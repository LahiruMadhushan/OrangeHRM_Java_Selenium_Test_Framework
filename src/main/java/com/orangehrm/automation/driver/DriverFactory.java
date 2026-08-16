package com.orangehrm.automation.driver;

import com.orangehrm.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates and tears down WebDriver instances. Each thread gets its own driver
 * so the framework stays safe for parallel TestNG execution.
 * Browser binaries are resolved automatically by Selenium Manager (bundled
 * with Selenium 4.6+), so no manual driver downloads/paths are required.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver(ConfigReader.getBrowser()));
        }
        return DRIVER.get();
    }

    private static WebDriver createDriver(String browser) {
        WebDriver driver = switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (ConfigReader.isHeadless()) {
                    options.addArguments("-headless");
                }
                yield new FirefoxDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (ConfigReader.isHeadless()) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--start-maximized");
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        driver.manage().window().maximize();
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
