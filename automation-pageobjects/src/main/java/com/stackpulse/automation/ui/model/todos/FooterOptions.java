package com.stackpulse.automation.ui.model.todos;

import com.stackpulse.automation.ui.model.BasePage;
import com.stackpulse.automation.ui.model.FooterOptionsEnum;
import com.stackpulse.automation.ui.selenium.DriverWrapper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class FooterOptions extends BasePage {
    @FindBy(how = How.CSS, using ="ul.filters li")
    private List<WebElement> filtersButtonList;
    @FindBy(how = How.CSS, using ="span.todo-count strong")
    private WebElement todoCountElem;
    @FindBy(how = How.CSS, using ="button.clear-completed")
    private WebElement clearCompletedButton;
    @FindBy(how = How.CSS, using = "span.todo-count")
    private WebElement noItemsLeftElem;

    public FooterOptions(DriverWrapper driver){
        super(driver);
    }

    public void selectAction(FooterOptionsEnum footerOptionsEnum){
        for(WebElement elem : filtersButtonList){
            if(elem.getText().equals(footerOptionsEnum.value)){
                elem.click();
            }
        }
    }

    public String getCountText(){
        return noItemsLeftElem.getText();
    }

    public void clickClearCompletedButton(){
        clickIfVisible(clearCompletedButton);
    }

    public String activeTasksCount(){
        return todoCountElem.getText();
    }
}
