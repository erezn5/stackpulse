package com.stackpulse.automation.ui.model.todos;

import com.stackpulse.automation.ui.model.BasePage;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static com.stackpulse.automation.framework.logger.LoggerFactory.LOG;

public class TopBar extends BasePage {
    @FindBy(how = How.CSS, using="input.new-todo")
    private WebElement insertNewTaskInput;

    @FindBy(how = How.CSS, using="input.toggle-all")
    private WebElement toggleAllButton;

    @FindBy(how = How.CSS, using="button.clear-completed")
    private WebElement clearCompletedButton;

    public TopBar(DriverWrapper driver){
        super(driver);
    }

    public void setNewTask(String... tasksDesc){
        for(String taskDesc: tasksDesc) {
            setText(insertNewTaskInput, taskDesc);
        }
    }

    public void clickToggleButton(){
        clickIfVisible(toggleAllButton);
    }

    public void toggleAll(){
        LOG.i("Toggling all tasks");
        clickToggleButton();
        LOG.i("Clearing all tasks");
        clickIfVisible(clearCompletedButton);
    }




}
