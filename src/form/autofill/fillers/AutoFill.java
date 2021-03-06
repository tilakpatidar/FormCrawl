package form.autofill.fillers;

import form.Form;
import form.autofill.data.Record;

/**
 * Abstract class to be extended for all types of fillers
 */
public abstract class AutoFill {

  private Form form;

  public void init(Form form) {
    this.form = form;
  }
  public abstract void fill(Record record);
  public Form getForm() {
    return form;
  }
}
