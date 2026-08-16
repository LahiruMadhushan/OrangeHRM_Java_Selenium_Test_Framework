package com.orangehrm.automation.base;

import com.orangehrm.automation.config.ConfigReader;
import com.orangehrm.automation.driver.DriverFactory;
import com.orangehrm.automation.pages.LoginPage;
import com.orangehrm.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Owns the WebDriver lifecycle and navigation to the login page for every
 * test class, so individual tests only deal with page objects and
 * assertions.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getAppUrl());
        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtils.capture(driver, result.getName());
        }
        DriverFactory.quitDriver();
    }
}
