package com.stackpulse.automation.ui.model;


import com.stackpulse.automation.framework.conf.EnvConf;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.support.PageFactory;

import static com.stackpulse.automation.framework.logger.LoggerFactory.LOG;

public class BasePage extends PageElement {
    private final String url;

    protected final static String URL_ADDRESS= EnvConf.getProperty("base.url");

    public BasePage(DriverWrapper driver){
        super(driver);
        this.url = URL_ADDRESS;
        PageFactory.initElements(driver, this);
    }

    private void navigate(){
        driver.get(url);
        LOG.i("Navigate to url=[%s]", url);
    }

    private void refresh(){
        LOG.i("refresh url '%s'", driver.getCurrentUrl());
        driver.navigate().refresh();
    }

    public void navigateAndVerify(){
        if(url.equals(driver.getCurrentUrl())){
            refresh();
        }else{
            navigate();
        }
        LOG.i("navigation succeeded");
    }
}
