package form.autofill.data;

import java.util.HashMap;

/**
 * Represents a tuple of data with key as css selector and value as
 * form value
 */
public class Record extends HashMap<String, String> {
  public Record(){
    super();
    super.put("#user_email", "tilakpatidar@gmail.com");
    super.put("#user_login", "tilakpatidar");
    super.put("#user_password", "tilakpatidar@");
  }
}
