package com.orangehrm.automation.pages;

import com.orangehrm.automation.config.ConfigReader;
import com.orangehrm.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Common Selenium actions shared by every page object. Concrete pages only
 * define their own locators and business-meaningful actions; low-level
 * WebDriver interaction lives here so it is never duplicated.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver, ConfigReader.getExplicitWaitSeconds());
    }

    protected void click(By locator) {
        waitUtils.waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitUtils.waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitUtils.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
