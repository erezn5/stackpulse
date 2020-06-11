package com.stackpulse.automation.tests.ui;

import com.stackpulse.automation.tests.BaseTest;
import com.stackpulse.automation.ui.model.todos.TodosPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

import static com.stackpulse.automation.framework.logger.LoggerFactory.LOG;

public class TodosTest extends BaseTest {
    private TodosPage todosPage;

    @BeforeClass
    public void setup() {
        todosPage = new TodosPage(driverWrapper);
    }

    @Test(priority = 5)
    public void navigateToTodoPage(){
        todosPage.navigateAndVerify();
    }

    @Test(priority = 10)
    public void insertNewTask(){
        todosPage.setNewTask("wash the dishes", "take the garbage out");
        Assert.assertEquals(todosPage.activeTasksCount(), "3");
        LOG.info("Tasks added successfully");
        todosPage.destroyTask(true,"take the garbage out");
    }

    @Test(priority = 15)
    public void verifyCompletedTasks(){
        todosPage.setNewTask("take the garbage out");
        todosPage.markTaskAsDone(true,"take the garbage out");
        Assert.assertEquals(getCompletedTask(todosPage.getCompletedTasks(), "take the garbage out").getText(), "take the garbage out");
        Assert.assertEquals(todosPage.getCompletedTasks().size(), 1);
        LOG.info("Verification of completed task passed successfully");
    }

    private WebElement getCompletedTask(List<WebElement> completedTasksList, String completedTask){
        return completedTasksList.stream()
                .filter(elem -> completedTask.equals(elem.getText()))
                .findFirst().orElse(null);
    }

    @Test(priority = 20)
    public void verifyActiveTasks(){
        todosPage.getActiveTasks();
        Assert.assertNull(getCompletedTask(todosPage.getActiveTasks(),
                "take the garbage out"));//we already marked this task as done in step 15,
                                                    // so we are checking that the task is not active anymore
        Assert.assertEquals(todosPage.getActiveTasks().size(), 2);
        LOG.info("Verifying active tasks passed successfully");
    }

    @Test(priority = 25)
    public void clearAllTasksAndVerify(){
        todosPage.toggleAll();
        int size = todosPage.getActiveTasks().size();
        Assert.assertEquals(size, 0, String.format("List should be empty, actual size is=[%d]", size));
        LOG.info("List is empty as expected");
    }

    @Test(priority = 30)
    public void markAllTasksAsDone(){
        todosPage.setNewTask("Take the dog out", "Ordering a new table", "Preparing for next day");
        todosPage.clickToggleAllButton();
        Assert.assertEquals(todosPage.getCountText(), "No items left");
        LOG.info("No items left as expected");
    }
}
