package form.autofill.fillers;

import form.Form;
import form.Input;
import form.autofill.suggesters.Suggester;
import form.inputs.Group;

import java.util.ArrayList;

public class BasicAutoFill extends AutoFill {

  @Override
  public void fill(Suggester suggester) {
    Form form = super.getForm();
//    Record record = suggester.getSuggestedRecord();
//    for(String selector : record.keySet()){
//      Input input = form.findByCSSSelector(selector);
//      input.fill(record.get(selector));
//    }
    for(Group group : form.getInputGroups()){
      ArrayList<Input> inputs = group.getElements();
      for(Input input : inputs){
        input.fill("");
      }
    }
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void fill(Suggester suggester, String html, String text) {

  }
}
