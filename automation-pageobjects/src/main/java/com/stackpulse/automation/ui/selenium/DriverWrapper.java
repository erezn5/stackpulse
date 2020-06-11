package com.stackpulse.automation.ui.selenium;

import com.stackpulse.automation.framework.conf.EnvConf;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;


public class DriverWrapper implements WebDriver {
    private static WebDriver driver;
    private DriverWrapper(WebDriver driver) {
        DriverWrapper.driver = driver;
    }

    public static DriverWrapper open(Browser browser, File downloadsFolder) {
        switch (browser) {
            case FIREFOX:
                return createFireFoxInst();
            case CHROME:
                return createChromeInst(downloadsFolder);
            default:
                throw new IllegalArgumentException("'" + browser + "'no such browser type");
        }
    }

    private static DriverWrapper createFireFoxInst() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless((EnvConf.getAsBoolean("selenium.headless")));
        options.addArguments("--window-size=1920,1080");
        driver = new FirefoxDriver(options);
        return new DriverWrapper(driver);
    }

//    public WebElement clickOnEnabledElm(Duration duration, By by) {
//        WebDriverWait wait = new WebDriverWait(driver, duration.getTimeUnit().toSeconds(duration.getValue()));
//        return wait.until(WaitConditions.elementEnabled(by));
//    }

    public File getScreenshotAsFile() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    private static DriverWrapper createChromeInst(File downloadsFolder) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(EnvConf.getAsBoolean("selenium.headless"));
        options.addArguments("--window-size=" + EnvConf.getProperty("selenium.window_size"));

        options.addArguments("--lang=" + EnvConf.getProperty("selenium.locale"));
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.SEVERE);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        ChromeDriverService service = ChromeDriverService.createDefaultService();
        driver = new ChromeDriver(service, options);
//        enableHeadlessDownload(service, chromeDriver, downloadsFolder);
        return new DriverWrapper(driver);
    }

    public boolean isVisible(By by, Duration duration) {
        try {
            waitForElmVisibility(duration, by);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Override
    public void get(String s) {
        driver.get(s);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    public WebElement waitForElmClickable(Duration duration, By by) {
        WebDriverWait wait = new WebDriverWait(driver, duration.getTimeUnit().toSeconds(duration.getValue()));
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement waitForElmVisibility(By by) {
        return waitForElmVisibility(Duration.TEN_SECONDS, by);
    }

    public boolean waitForWebElementVisibility(WebElement elem){
        return elem.isDisplayed();
    }

    public WebElement waitForElmVisibility(Duration duration, By by) {
        WebDriverWait wait = new WebDriverWait(driver, duration.getTimeUnit().toSeconds(duration.getValue()));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
