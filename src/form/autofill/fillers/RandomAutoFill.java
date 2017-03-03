package form.autofill.fillers;

import form.Form;
import form.Input;
import form.autofill.data.Record;
import form.autofill.suggesters.Suggester;
import form.inputs.Group;

import java.util.ArrayList;

public class RandomAutoFill extends AutoFill {
  @Override
  public void fill(Suggester suggester) {
    Form form = super.getForm();
    Record record = suggester.getSuggestedRecord();
    String titleSearch = record.get("#title_search");
    form.findByCSSSelector("#title_search").fill(titleSearch);
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
