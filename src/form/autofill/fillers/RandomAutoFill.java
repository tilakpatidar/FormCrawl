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
import java.util.stream.Collectors;

public class RandomAutoFill extends AutoFill {
  @Override
  public void fill(Suggester suggester) {
    Form form = super.getForm();
    Record record = suggester.getSuggestedRecord();
    String titleSearch = record.get("#title_search");
    By by = By.cssSelector("#title_search");
    WebDriver driver = form.getAssociatedPage().getDriver();
    WebElement inputElement = driver.findElement(by);
    Input desiredInput = form.getAssociatedInputs().stream().filter((e) -> e.getWebElement().equals(inputElement)).collect(Collectors.toList()).get(0);
    desiredInput.fill(titleSearch);
    for (Group group : form.getInputGroups()) {
      ArrayList<Input> inputs = group.getElements();
      for (Input input : inputs) {
        if (false) {
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
