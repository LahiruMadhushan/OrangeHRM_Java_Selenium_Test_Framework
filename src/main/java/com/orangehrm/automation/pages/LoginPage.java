package com.orangehrm.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the OrangeHRM login page
 * (/web/index.php/auth/login).
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By invalidCredentialsAlert = By.cssSelector(".oxd-alert-content-text");
    private final By dashboardHeader = By.cssSelector(".oxd-topbar-header-breadcrumb-module");

    private final By usernameRequiredMessage = By.xpath(
            "//input[@name='username']/ancestor::div[contains(@class,'oxd-input-group')][1]" +
                    "//span[contains(@class,'oxd-input-field-error-message')]");
    private final By passwordRequiredMessage = By.xpath(
            "//input[@name='password']/ancestor::div[contains(@class,'oxd-input-group')][1]" +
                    "//span[contains(@class,'oxd-input-field-error-message')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Fills both fields and submits the form in one call, for the common
     * happy-path/negative scenarios that just need a login attempt.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getInvalidCredentialsMessage() {
        return getText(invalidCredentialsAlert);
    }

    public boolean isInvalidCredentialsMessageDisplayed() {
        return isDisplayed(invalidCredentialsAlert);
    }

    public String getUsernameRequiredMessage() {
        return getText(usernameRequiredMessage);
    }

    public boolean isUsernameRequiredMessageDisplayed() {
        return isDisplayed(usernameRequiredMessage);
    }

    public String getPasswordRequiredMessage() {
        return getText(passwordRequiredMessage);
    }

    public boolean isPasswordRequiredMessageDisplayed() {
        return isDisplayed(passwordRequiredMessage);
    }

    public boolean isLoginSuccessful() {
        return getCurrentUrl().contains("/dashboard") && isDisplayed(dashboardHeader);
    }
}
