package form.inputs;

import form.Input;

import java.util.ArrayList;

/**
 * Created by tilak on 4/12/16.
 */
public abstract class Group {

  private ArrayList<Input> inps;
  private Group.GROUP_TYPES type;
  private String name;

  public Group(String name, Group.GROUP_TYPES type) {
    this.type = type;
    this.name = name;
  }

  ;
  public void addElement(Input check) {
    if (check instanceof CheckBox || check instanceof Radio) {
      inps.add(check);
    }
  }
  public String getName() {
    return this.name;
  }
  public ArrayList<Input> getElements() {

    return this.inps;
  }
  public int getSize() {
    return this.inps.size();
  }
  public Group.GROUP_TYPES getType() {
    return this.type;
  }

  public static enum GROUP_TYPES {
    CHECKBOX_GROUP, RADIO_GROUP
  }
}
