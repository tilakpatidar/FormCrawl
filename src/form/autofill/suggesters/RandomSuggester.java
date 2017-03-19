package form.autofill.suggesters;

import form.Form;
import form.Input;
import form.autofill.data.Record;
import form.inputs.Group;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSuggester extends Suggester {
  @Override
  public Record getSuggestedRecord(Form form) {
    Record record = new Record();
    List<Input> unboundedFields = form.getUnboundedFields();
    unboundedFields.forEach(field -> record.put(field, ""));

    //filling groupable and bounded elements
    for (Group group : form.getInputGroups()) {
      if (randomTruth()) {
        ArrayList<Input> inputs = group.getElements();
        boolean filled = false;
        for (Input input : inputs) {
          if (randomTruth() && !filled) {
            filled = true;
            record.put(input, "");
          }
        }
      }
    }

    //filling non groupable but bounded elements
    //basically select tags
    for (Input input : form.getNonGroupableBoundedInputs()) {
      By option = By.tagName("option");
      List<WebElement> optionTags = input.getWebElement().findElements(option);
      Random randomizer = new Random();
      int randomIndex = randomizer.nextInt(optionTags.size());
      WebElement randomOption = optionTags.get(randomIndex);
      record.put(input, randomOption.getText());
    }
    return record;
  }

  private boolean randomTruth() {
    int randomNumber = (int) (Math.random() * 100);
    return randomNumber < 20 || randomNumber > 80;
  }
}
