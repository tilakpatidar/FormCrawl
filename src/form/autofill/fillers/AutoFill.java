package form.autofill.fillers;

import form.Form;
import form.autofill.data.Record;

import java.util.ArrayList;

/**
 * Abstract class to be extended for all types of fillers
 */
public abstract class AutoFill {

  private Form form;
  private ArrayList<Record> records;

  public void init(Form form) {
    this.form = form;
    this.records = new ArrayList<Record>();
  }
  public abstract void fill(Record record);
  public Form getForm() {
    return form;
  }
}
