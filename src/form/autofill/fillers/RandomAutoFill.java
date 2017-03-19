package form.autofill.fillers;

import form.Form;
import form.Input;
import form.autofill.data.Record;
import form.autofill.suggesters.Suggester;
import form.inputs.Group;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class RandomAutoFill extends AutoFill {
  @Override
  public void fill(Suggester suggester) {
    fill(suggester, "", "");
  }
  @Override
  public void fill(Suggester suggester, String html, String text) {
    Form form = super.getForm();
    Record record = suggester.getSuggestedRecord();
    String titleSearch = record.get("#title_search");
    By by = By.cssSelector("#title_search");
    WebDriver driver = form.getAssociatedPage().getDriver();
    WebElement inputElement = driver.findElement(by);

    Input desiredInput = form.getInputBy(inputElement);
    desiredInput.fill(titleSearch);
    for (Group group : form.getInputGroups()) {
      ArrayList<Input> inputs = group.getElements();
      boolean filled = false;
      for (Input input : inputs) {
        if (randomTruth() && !filled) {
          filled = true;
          input.fill("");
        }
      }
    }
  }

  private boolean randomTruth() {
    int randomNumber = (int) (Math.random() * 100);
    return randomNumber < 20 || randomNumber > 80;
  }
}
