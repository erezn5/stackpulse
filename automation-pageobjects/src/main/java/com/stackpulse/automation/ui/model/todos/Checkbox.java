package com.stackpulse.automation.ui.model.todos;


import com.stackpulse.automation.ui.model.AbstractCheckbox;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.By;

public class Checkbox extends AbstractCheckbox {

    public Checkbox(DriverWrapper driver, By toggleCheckBox) {
        super(driver, toggleCheckBox);
    }

    @Override
    protected boolean isCheck() {
        return isVisible(By.cssSelector("ul.todo-list li.completed"));
    }
}
