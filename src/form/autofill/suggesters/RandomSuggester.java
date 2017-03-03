package form.autofill.suggesters;

import form.autofill.data.Record;

public class RandomSuggester extends Suggester {
  @Override
  public Record getSuggestedRecord() {
    Record record = new Record();
    record.put("#title_search", "the godfather");
    return record;
  }
}
