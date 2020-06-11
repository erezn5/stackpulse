package com.stackpulse.automation.ui.components;

import com.stackpulse.automation.ui.model.PageElement;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.By;
import static com.stackpulse.automation.framework.logger.LoggerFactory.LOG;

public abstract class AbstractToggleButton extends PageElement {

    protected final By leftButton;
    protected final By rightButton;

    protected abstract boolean isLeftPressed();

    protected AbstractToggleButton(DriverWrapper driver, By leftButton, By rightButton) {
        super(driver);
        this.leftButton = leftButton;
        this.rightButton = rightButton;
    }

    public void switchLeft() {
        if (isLeftPressed()) {
            LOG.i("left toggle button already pressed");
        } else {
            clickButton(leftButton);
            throwChangeElementStateException(isLeftPressed(), leftButton.toString(), "selected");
        }
    }

    public void switchRight() {
        if (isLeftPressed()) {
            clickButton(rightButton);
            throwChangeElementStateException(!isLeftPressed(), rightButton.toString(), "selected");
        } else {
            LOG.i("right toggle button already pressed");
        }
    }

}
