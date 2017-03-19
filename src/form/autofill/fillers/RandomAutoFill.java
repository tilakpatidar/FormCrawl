package form.autofill.fillers;

import form.autofill.data.Record;

public class RandomAutoFill extends AutoFill {
  @Override
  public void fill(Record record) {
    record.entrySet().forEach(set -> set.getKey().fill(set.getValue()));
  }
}
