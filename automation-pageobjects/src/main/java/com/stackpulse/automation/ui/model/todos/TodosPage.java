package com.stackpulse.automation.ui.model.todos;

import com.stackpulse.automation.ui.model.AbstractCheckbox;
import com.stackpulse.automation.ui.model.BasePage;
import com.stackpulse.automation.ui.model.FooterOptionsEnum;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class TodosPage extends BasePage {
    @FindBy(how = How.CSS, using ="ul.todo-list li")
    List<WebElement> todoListElems;
    @FindBy(how= How.CSS, using="div.todoapp")
    private WebElement appTitleElem;
    @FindBy(how= How.CSS, using="input.toggle")
    private WebElement toggleTaskButton;
    @FindBy(how= How.CSS, using="button.destroy")
    private WebElement destroyTaskButton;

    TopBar topBar;
    FooterOptions footerOptions;

    public TodosPage(DriverWrapper driver){
        super(driver);
        topBar = new TopBar(driver);
        footerOptions = new FooterOptions(driver);
    }

    public void setNewTask(String... tasksDesc){
        topBar.setNewTask(tasksDesc);
    }

    //This function control how we handle tasks (cross over the task or undo)
    public void destroyTask(boolean mode, String taskName){
        markTaskAsDone(mode, taskName);
        clickButtonRetry(destroyTaskButton, taskName);
    }

    public void markTaskAsDone(boolean mode, String... taskNames) {
        By checkboxBy = createLocatorFromString("ul.todo-list input[type='checkbox']");
        AbstractCheckbox checkbox = new Checkbox(driver, checkboxBy);
        for (WebElement elem : todoListElems) {
            for(String taskName : taskNames) {
                if (elem.getText().equals(taskName)) {
                    checkbox.check(mode);
                }
            }
        }
    }

    private By createLocatorFromString(String str){
        return By.cssSelector(str);
    }

    public void selectAction(FooterOptionsEnum footerOptionsEnum){
        footerOptions.selectAction(footerOptionsEnum);
    }

    public String activeTasksCount(){
        return footerOptions.activeTasksCount();
    }

    public List<WebElement> getCompletedTasks(){
        footerOptions.selectAction(FooterOptionsEnum.COMPLETED);
        return todoListElems;
    }

    public List<WebElement> getActiveTasks(){
        footerOptions.selectAction(FooterOptionsEnum.ACTIVE);
        return todoListElems;
    }

    public void toggleAll(){
        topBar.toggleAll();
    }

    public String getCountText(){
        return footerOptions.getCountText();
    }

    public void clickToggleAllButton(){
        topBar.clickToggleButton();
    }

}
