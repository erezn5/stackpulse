package com.stackpulse.automation.ui.model;


import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.By;

public abstract class AbstractCheckbox extends PageElement {

    private final By checkboxBy;
    protected abstract boolean isCheck();

    protected AbstractCheckbox(DriverWrapper driver, By checkboxBy){
        super(driver);
        this.checkboxBy = checkboxBy;
    }

    public void check(boolean check){
        if(isCheck() ^ check){
            driver.findElement(checkboxBy).click();
        }
    }
}
