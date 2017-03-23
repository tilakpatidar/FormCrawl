/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import org.openqa.selenium.By;
import form.util.WElement;
import org.openqa.selenium.support.ui.ISelect;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Select input implementation
 *
 * @author tilak
 */
public class Select extends Input {

  private final boolean multi_select;
  private final HashMap<String, String> select_values;

  public Select(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.SELECT_INPUT);
    this.multi_select = isMultipleSelect();
    this.select_values = new HashMap();
    this.storeSelectValues();
  }
  private boolean isMultipleSelect() {
    return this.getWElement().getAttribute("multiple") != null;
  }

  /**
   * Select the option with the text matching the value attr.
   * @param s
   */
  @Override
  public void fill(String s) {
    selectInstance().selectByVisibleText(s); // TODO using default value in 
    // select
  }
  private ISelect selectInstance() {
    return new org.openqa.selenium.support.ui.Select(this
        .getWElement().ge());
  }
  @Override
  public boolean isBounded() {
    return true;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }

  /**
   * Selects the options with the text matching the value attr. Only for
   * multiple attr selects
   *
   * @param vals
   */
  private void fillValues(String[] vals) throws UnsupportedOperationException {
    if (!this.isMultiSelect()) {
      throw new UnsupportedOperationException("Not supported for non multiple select");
    }
    for (String val : vals) {
      selectInstance().selectByValue(val);
    }
  }

  /**
   * Selects the option that displays the text matching the parameter.
   *
   * @param text
   */
  private void fillByVisibleText(String text) {
    selectInstance().deselectByVisibleText(text);
  }

  /**
   * Selects the options that displays the text matching the parameters.
   * Only for multiple attr selects
   *
   * @param texts
   */
  private void fillByVisibleText(String[] texts) throws UnsupportedOperationException {
    if (!this.isMultiSelect()) {
      throw new UnsupportedOperationException("Not supported for non multiple select");
    }
    for (String text : texts) {
      selectInstance().deselectByVisibleText(text);
    }
  }

  /**
   * Using select dom creates a HashMap of value and label
   */
  private void storeSelectValues() {
    WElement select = this.getWElement();
    List<WElement> options = select.findElements(By.tagName("option"));
    for (WElement option : options) {
      this.select_values.put(option.getAttribute("value"), option.getText());
    }
  }

  public boolean isMultiSelect() {
    return this.multi_select;
  }
}
