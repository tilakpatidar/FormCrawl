package form.autofill.fillers;

import form.Form;
import form.autofill.data.Record;
import form.autofill.suggesters.Suggester;

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
  public abstract void fill(Suggester suggester);
  public abstract void fill(Suggester suggester, String html, String text);
  public Form getForm() {
    return form;
  }
}
