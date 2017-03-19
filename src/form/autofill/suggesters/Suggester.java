package form.autofill.suggesters;

import form.Form;
import form.autofill.data.Record;

public abstract class Suggester {

  public abstract Record getSuggestedRecord(Form form);
}
