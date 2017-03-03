package form.autofill.suggesters;

import form.autofill.data.Record;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Suggester {

  private static HashMap<String, ArrayList<Record>> dataStore;

  public static void put(String actionURL, ArrayList<Record> records) {
    Suggester.dataStore.put(actionURL, records);
  }

  public static ArrayList<Record> get(String actionURL) {
    return Suggester.dataStore.get(actionURL);
  }

  public abstract Record getSuggestedRecord();
}
