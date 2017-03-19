package form.autofill.data;

import form.Input;

import java.util.HashMap;

/**
 * Represents a tuple of data with key as css selector and value as
 * form value
 */
public class Record extends HashMap<Input, String> {
  public Record(){
    super();
  }
}
