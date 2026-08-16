# OrangeHRM Login Automation Framework

A clean, interview-ready **Selenium WebDriver + TestNG + Maven** test automation
framework built with the **Page Object Model (POM)**, covering login scenarios
for the [OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login).

## Overview

The framework is intentionally small: a handful of well-named classes, each
with one responsibility, rather than a large stack of abstractions. Framework
code (driver management, waits, page objects) lives under `src/main/java`;
test code (test classes, test data) lives under `src/test/java`.

Key design points:

- **Page Object Model** — Selenium locators and low-level actions live in
  page objects; assertions live in test classes.
- **`BasePage`** centralizes shared WebDriver actions (`click`, `type`,
  `getText`, `isDisplayed`) so page objects stay declarative.
- **`DriverFactory`** creates/quits WebDriver instances via a `ThreadLocal`,
  keeping the door open for parallel execution, and supports Chrome/Firefox
  through configuration. Browser binaries are resolved automatically by
  Selenium's built-in Selenium Manager (Selenium 4.6+) — no manual driver
  downloads or `webdriver.chrome.driver` paths required.
- **`ConfigReader`** loads app URL / browser / wait timeout from
  `config.properties`, with optional override via `-D` system properties
  (handy for CI).
- **`TestDataReader`** loads login credentials from a test-only
  `login-data.properties` file — no credentials are hardcoded in test classes.
- **Explicit waits only** (`WaitUtils`), no `Thread.sleep()`.
- **`ScreenshotUtils`** captures a screenshot on test failure — deliberately
  simple, no reporting framework attached.

## Project Structure

```text
OrangeHRM Automation/
├── pom.xml
├── testng.xml
├── README.md
├── .gitignore
│
├── src/main/java/com/orangehrm/automation/
│   ├── config/
│   │   └── ConfigReader.java        # reads config.properties (+ -D overrides)
│   ├── driver/
│   │   └── DriverFactory.java       # creates/quits WebDriver (Chrome/Firefox)
│   ├── pages/
│   │   ├── BasePage.java            # shared Selenium actions + explicit waits
│   │   └── LoginPage.java           # login page locators + actions
│   └── utils/
│       ├── WaitUtils.java           # WebDriverWait wrapper
│       └── ScreenshotUtils.java     # screenshot-on-failure helper
│
├── src/main/resources/
│   └── config.properties            # app URL, browser, wait timeout
│
└── src/test/
    ├── java/com/orangehrm/automation/
    │   ├── base/
    │   │   └── BaseTest.java        # WebDriver lifecycle (@BeforeMethod/@AfterMethod)
    │   ├── data/
    │   │   └── TestDataReader.java  # reads login-data.properties
    │   └── tests/
    │       └── LoginTest.java       # login scenarios
    │
    └── resources/testdata/
        └── login-data.properties    # valid/invalid credentials
```

## Prerequisites

- **Java 17+** (JDK)
- **Maven 3.8+**
- **Google Chrome** installed (default browser; Firefox also supported)
- Internet access (to reach the OrangeHRM demo app and for Selenium Manager
  to resolve driver binaries on first run)

## Configuration

Edit `src/main/resources/config.properties`:

```properties
app.url=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
browser=chrome
explicit.wait.seconds=10
headless=false
```

To switch browsers or run headless without editing the file, pass system
properties on the command line:

```bash
mvn test -Dbrowser=firefox
mvn test -Dheadless=true
```

## Running the Tests

From the project root:

```bash
mvn test
```

This compiles the project and runs `testng.xml`, which executes the
`LoginTest` suite. Run a single test class directly if needed:

```bash
mvn test -Dtest=LoginTest
```

Screenshots for any failed test are saved to `screenshots/` in the project
root, named `<testName>_<timestamp>.png`.

## Login Test Coverage

`LoginTest` covers 7 distinct, non-overlapping scenarios (redundant
combinations that would duplicate an existing assertion, such as "empty
username + empty password" vs. a separate standalone "username required"
check, are intentionally merged into one representative test):

| # | Scenario | Expected Result |
|---|----------|------------------|
| 1 | Valid username + valid password | Successful login, navigates to Dashboard |
| 2 | Invalid username + invalid password | "Invalid credentials" error shown |
| 3 | Valid username + invalid password | "Invalid credentials" error shown |
| 4 | Invalid username + valid password | "Invalid credentials" error shown |
| 5 | Empty username + empty password | Username "Required" validation shown |
| 6 | Empty username + valid password | Username "Required" validation shown |
| 7 | Valid username + empty password | Password "Required" validation shown |

All 7 tests have been executed against the live application and pass.

## Important Design Decisions

- **No WebDriverManager dependency** — Selenium 4.6+ ships Selenium Manager,
  which resolves the correct browser driver automatically, so an extra
  third-party dependency isn't needed.
- **Config vs. test data are kept separate** — `config.properties` describes
  *environment* (URL, browser, timeouts); `login-data.properties` describes
  *test input* (credentials). Mixing the two would blur framework config with
  test data ownership.
- **`ThreadLocal` WebDriver** in `DriverFactory` — makes the framework safe
  for parallel TestNG execution later without any rework.
- **No reporting framework** — TestNG's own report plus a screenshot on
  failure is enough for this scope; adding ExtentReports/Allure would be
  over-engineering for a login suite.
