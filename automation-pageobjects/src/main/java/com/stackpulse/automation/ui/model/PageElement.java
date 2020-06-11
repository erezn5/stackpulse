package com.stackpulse.automation.ui.model;

import com.stackpulse.automation.framework.conf.EnvConf;
import com.stackpulse.automation.framework.utils.Waiter;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.awaitility.Duration;
import org.awaitility.core.Condition;
import org.openqa.selenium.*;
import java.io.File;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.stackpulse.automation.framework.logger.LoggerFactory.LOG;


public class PageElement {

    protected static final Duration WAIT_TIMEOUT = new Duration(EnvConf.getAsInteger("ui.locator.timeout.sec") , TimeUnit.SECONDS);
    protected static final int CONDITION_RETRY = EnvConf.getAsInteger("ui.locator.action.retry");
    protected final DriverWrapper driver;

    protected PageElement(DriverWrapper driver) {
        this.driver = driver;
    }

    protected void clickButtonWithDelay(By byBth){
        clickButton(byBth, WAIT_TIMEOUT);
    }
    protected void clickButton(By byBth){
        clickButton(byBth, WAIT_TIMEOUT);
    }

    protected void clickButton(By byBth, Duration timeout){
        WebElement bthElem = waitForClickableElm(byBth, timeout);
        clickButton(bthElem);
    }

    protected void clickButtonRetry(final WebElement bthElm, final String locator){
        throwElmNotFound(bthElm, locator);
        Condition<Boolean> condition = () -> {
            try{
                clickButton(bthElm);
                LOG.i("button=[%s] clicked successfully" , locator);
                return true;
            }catch (Exception e){
                LOG.d(e , "failed to click on button=[%s]" , locator);
                return false;
            }
        };
        Boolean success = Waiter.waitCondition(WAIT_TIMEOUT , condition);
        throwElmNotClickable(success, locator);
    }

    protected void clickButtonRetry(final By bthBy){
        final String locator = bthBy.toString();
        Condition<Boolean> condition = () -> {
            try{
                clickButton(bthBy);
                LOG.i("button=[%s] clicked successfully" , locator);
                return true;
            }catch (Exception e){
                LOG.d(e , "failed to click on button=[%s]" , locator);
                return false;
            }
        };
        Duration timeout = new Duration(WAIT_TIMEOUT.getValueInMS() * CONDITION_RETRY , TimeUnit.MILLISECONDS);
        Boolean success = Waiter.waitCondition(timeout , condition);
        throwElmNotClickable(success , locator);
    }

    protected void clickButton(WebElement bthElm){
        bthElm.click();
        printClick(bthElm);
    }

    private void clearAndSetText(WebElement inputElm , String text){
        inputElm.click();
        sleep(Duration.TWO_HUNDRED_MILLISECONDS);
        inputElm.clear();
        sleep(Duration.TWO_HUNDRED_MILLISECONDS);
        inputElm.sendKeys(text);
        printSet(inputElm , text);
    }

    protected void setText(WebElement txtElm , String text){
        txtElm.sendKeys(text);
        txtElm.sendKeys(Keys.ENTER);
        printSet(txtElm , text);
    }

    protected WebElement waitForElement(By by){
        return waitForElement(by, WAIT_TIMEOUT);
    }

    private WebElement waitForElement(By by, Duration duration){
        WebElement element = driver.waitForElmVisibility(duration, by);
        printElmVisibility(by , (element != null));
        return element;
    }

    private void printClick(WebElement elementBth){
        LOG.i("click on '%s' button" , elementBth);
    }

    private void printSet(WebElement txtElm , String txt){
        LOG.i("set '%s' with value '%s'" , txtElm , txt);
    }

    private void printElements(List<WebElement> elements){
        LOG.i("elements [%s]" , getElementsText(elements));
    }

    private void printElmText(By elmBy , String content){
        LOG.i("locator=[%s] contains text=[%s]" , elmBy , content);
    }

    private void printElmTextNotFound(By elmBy , String content){
        LOG.i("locator=[%s] NOT contains text=[%s]" , elmBy , content);
    }

    protected void clickIfVisible(WebElement elem){
        if(elem.isDisplayed()){
            clickButton(elem);
        }
    }

    private void printElmTextNotFound(WebElement element , String content){
        LOG.i("element=[%s] NOT contains text=[%s]" , element , content);
    }

    private void printElmAttr(By elmBy , String attribute , String attrValue){
        LOG.i("locator '%s' attr [%s='%s']" , elmBy , attribute , attrValue);
    }

    private void printElmAttrNotFound(By elmBy , String attribute){
        LOG.i("locator '%s' with content '%s' not found" , elmBy , attribute);
    }

    private String getElementsText(List<WebElement> elements){
        final StringBuffer sb = new StringBuffer();
        if(elements != null){
            elements.forEach(elm -> sb.append(elm.getText()).append(","));
            if(sb.length() > 0){
                sb.deleteCharAt(sb.length()-1);
            }
        }
        return sb.toString();
    }

    private void printElmText(WebElement element){
        LOG.i("element [%s]" , (element == null) ? "not found": element.getText());
    }

    protected boolean waitForElmBecomeVisible(By by){
        return waitForElmBecomeVisible(by, WAIT_TIMEOUT);
    }

    protected boolean waitForElmBecomeVisible(By by, Duration duration){
        boolean appear = driver.isVisible(by, duration);
        printElmVisibility(by, appear);
        return appear;
    }

    protected String getTextBy(By locatorBy){
        return driver.waitForElmVisibility(locatorBy).getText();
    }

    private void printElmVisibility(By by , boolean appear){
        LOG.d("locator=[%s] visible=[%b]" , by.toString() , appear);
    }

    protected boolean isVisible(By by){
        return isVisible(by, new Duration(3, TimeUnit.SECONDS));
    }

    protected boolean isVisible(WebElement elem){
        return elem.isDisplayed();
    }

    protected boolean isVisible(By by, Duration duration){
        boolean visible = driver.isVisible(by, duration);
        printElmVisibility(by , visible);
        return visible;
    }

    protected void clickIfVisible(By bthBy){
        if(isVisible(bthBy)){
            clickButton(bthBy);
        }else{
            LOG.i("locator '%s' not visible, skip on click" , bthBy);
        }
    }

    private void printSelect(By selectBy , boolean selected){
        LOG.i("'%s' locator -> %s" , selectBy , (selected ? "selected" : "not selected"));
    }

    // NOTICE: don't use it only if you must to!
    protected static void sleep(Duration duration){
        try {
            Thread.sleep(duration.getTimeUnit().toMillis(duration.getValue()));
        } catch (InterruptedException e) {
            LOG.e(e , "error occur while sleep, timeout=[%s]" , duration.toString());
            throw new RuntimeException(e);
        }
    }

    protected static void throwElmNotFound(Object object , String locator){
        if(object == null){
            throw new NoSuchElementException("failed to find locator=[" + locator + "]");
        }
    }

    protected static void throwElmNotFound(boolean notFound , String locator){
        if(notFound){
            throw new NoSuchElementException("failed to find locator=[" + locator + "]");
        }
    }

    protected static void throwChangeElementStateException(Boolean success , String locator , String value){
        if(success == null || !success){
            throw new ChangeElementStateException(String.format("failed to set locator=[%s] with value=[%s]" , locator , value));
        }
    }


    public static class ChangeElementStateException extends RuntimeException {
        ChangeElementStateException(String message) {
            super(message);
        }
    }

    protected static void throwElmNotClickable(Boolean clicked , String locator){
        if(clicked == null || !clicked){
            throw new ElementClickInterceptedException("failed to click on bth locator=[" + locator + "]");
        }
    }

    protected By createSpanContainsStringBy(String text){
        return By.xpath("//span[contains(string(), '" + text + "')]");
    }

    protected String getElmText(By by){
        return driver.waitForElmVisibility(WAIT_TIMEOUT, by).getText();
    }

    protected static String randSuffix(String prefix){
        return prefix + "_" + System.nanoTime();
    }

    protected WebElement waitForClickableElm(By by){
        return waitForClickableElm(by, WAIT_TIMEOUT);
    }

    private WebElement waitForClickableElm(By by, Duration timeout){
        WebElement element = driver.waitForElmClickable(timeout, by);
        printClickableElm(by);
        return element;
    }

    private void printClickableElm(By by){
        LOG.i("locator=[%s] is clickable" , by.toString());
    }

    private void printEnabledElm(By by){
        LOG.i("locator=[%s] is enabled" , by.toString());
    }


    protected static By createLocator(String str){
        return By.xpath(String.format("//div[contains(text(), '%s')]", str));
    }


}
