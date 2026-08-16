package com.orangehrm.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures a screenshot for a failed test and saves it under /screenshots.
 * Kept intentionally simple: no reporting framework, just a file on disk
 * named after the test so it's easy to locate.
 */
public final class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "screenshots";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Path targetDir = Path.of(SCREENSHOT_DIR);
            Files.createDirectories(targetDir);

            String fileName = testName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png";
            Path targetFile = targetDir.resolve(fileName);

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(sourceFile.toPath(), targetFile);

            return targetFile.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot for " + testName + ": " + e.getMessage());
            return null;
        }
    }
}
