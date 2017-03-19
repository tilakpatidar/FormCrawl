package form.inputs;

import form.Form;
import form.Input;
import org.openqa.selenium.WebElement;

import java.io.IOException;

/**
 * Created by tilak on 4/12/16.
 */
public abstract class Groupable extends Input {

  private String groupClassName;

  public Groupable(Form f, WebElement ip, FIELD_TYPES type) throws IOException {
    super(f, ip, type);

    //now create the group
    this.addToGroup(type);
  }

  private void addToGroup(FIELD_TYPES type) {
    Group gp = this.getAssociatedForm().findGroupByName(this.groupClassName, this.getInputName());
    if (gp == null) {
      //group not exists create one
      switch (type) {
        case CHECKBOX_INPUT:
          gp = new CheckBoxGroup(this.getInputName());
          gp.addElement(this);
          this.getAssociatedForm().getInputGroups().add(gp);
          break;
        case RADIO_INPUT:
          gp = new RadioGroup(this.getInputName());
          gp.addElement(this);
          this.getAssociatedForm().getInputGroups().add(gp);
          break;
      }
    } else {
      gp.addElement(this);
    }
  }

  public Group getGroup() {
    Group gp = this.getAssociatedForm().findGroupByName(this.groupClassName, this.getInputName());
    return gp;
  }
}
