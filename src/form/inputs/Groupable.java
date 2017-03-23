package form.inputs;

import form.Form;
import form.Input;
import form.util.WElement;

import java.io.IOException;

/**
 * Created by tilak on 4/12/16.
 */
public abstract class Groupable extends Input {

  private String groupClassName;

  public Groupable(Form f, WElement ip, FIELD_TYPES type) throws IOException {
    super(f, ip, type);

    //now create the group
    this.addToGroup(type);
  }

  private void addToGroup(FIELD_TYPES type) {
    String inputName = this.getInputName();
    Group gp = this.getAssociatedForm().getGroupBy(inputName);
    if (gp == null) {
      //group not exists create one
      switch (type) {
        case CHECKBOX_INPUT:
          gp = new CheckBoxGroup(inputName);
          gp.addElement(this);
          this.getAssociatedForm().addToGroup(gp);
          break;
        case RADIO_INPUT:
          gp = new RadioGroup(inputName);
          gp.addElement(this);
          this.getAssociatedForm().addToGroup(gp);
          break;
      }
    } else {
      gp.addElement(this);
    }
  }
}
