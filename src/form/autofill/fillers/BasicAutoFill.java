package form.autofill.fillers;

import form.Form;
import form.Input;

import java.util.ArrayList;

public class BasicAutoFill extends AutoFill {

  @Override
  public void fill() {
    Form form = super.getForm();
    ArrayList<Input> i = form.getAssociatedInputs();
    for (Input inp : i) {
      inp.fill();
    }
  }
}
