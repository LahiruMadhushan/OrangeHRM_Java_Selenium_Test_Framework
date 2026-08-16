package com.orangehrm.automation.tests;

import com.orangehrm.automation.base.BaseTest;
import com.orangehrm.automation.data.TestDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login scenarios for the OrangeHRM demo application. Each test represents
 * one distinct, non-overlapping combination of credential validity and
 * field emptiness; scenarios that would assert the same behavior twice
 * (e.g. "username required" for two different empty-field combinations)
 * are intentionally left as a single representative test.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Valid username and valid password logs the user in and lands on the Dashboard")
    
    public void testValidCredentials_LoginsSuccessfully() {
        loginPage.login(TestDataReader.validUsername(), TestDataReader.validPassword());
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "User should be navigated to the Dashboard after a valid login");
    }

    @Test(description = "Invalid username with invalid password shows the invalid credentials error")
    public void testInvalidUsernameAndInvalidPassword_ShowsInvalidCredentialsError() {
        loginPage.login(TestDataReader.invalidUsername(), TestDataReader.invalidPassword());

        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), "Invalid credentials");
    }

    @Test(description = "Valid username with invalid password shows the invalid credentials error")
    public void testValidUsernameAndInvalidPassword_ShowsInvalidCredentialsError() {
        loginPage.login(TestDataReader.validUsername(), TestDataReader.invalidPassword());

        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), "Invalid credentials");
    }

    @Test(description = "Invalid username with valid password shows the invalid credentials error")
    public void testInvalidUsernameAndValidPassword_ShowsInvalidCredentialsError() {
        loginPage.login(TestDataReader.invalidUsername(), TestDataReader.validPassword());

        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), "Invalid credentials");
    }

    @Test(description = "Submitting the form with both fields empty shows the username required validation")
    public void testEmptyUsernameAndEmptyPassword_ShowsUsernameRequiredValidation() {
        loginPage.login("", "");

        Assert.assertTrue(loginPage.isUsernameRequiredMessageDisplayed(),
                "Username 'Required' validation should be shown");
        Assert.assertEquals(loginPage.getUsernameRequiredMessage(), "Required");
    }

    @Test(description = "Empty username with a valid password shows the username required validation")
    public void testEmptyUsernameWithValidPassword_ShowsUsernameRequiredValidation() {
        loginPage.login("", TestDataReader.validPassword());

        Assert.assertTrue(loginPage.isUsernameRequiredMessageDisplayed(),
                "Username 'Required' validation should be shown");
        Assert.assertEquals(loginPage.getUsernameRequiredMessage(), "Required");
    }

    @Test(description = "Valid username with an empty password shows the password required validation")
    public void testValidUsernameWithEmptyPassword_ShowsPasswordRequiredValidation() {
        loginPage.login(TestDataReader.validUsername(), "");

        Assert.assertTrue(loginPage.isPasswordRequiredMessageDisplayed(),
                "Password 'Required' validation should be shown");
        Assert.assertEquals(loginPage.getPasswordRequiredMessage(), "Required");
    }
}
