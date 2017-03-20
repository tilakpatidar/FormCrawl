package form.autofill.data;

import form.Input;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static form.util.TextUtil.concatList;

/**
 * Represents a tuple of data with key as css selector and value as
 * form value
 */
public class Record extends HashMap<Input, String> {
  public Record(){
    super();
  }
  @Override
  public String toString() {
    List<String> collect = this.entrySet().stream().map(set -> set.getKey().getTitle() + " -> " + set.getValue()).collect(Collectors.toList());
    return "Record {\n" + concatList(collect, "\n") + "\n}";
  }
}
