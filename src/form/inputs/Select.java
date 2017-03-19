/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Select input implementation
 *
 * @author tilak
 */
public class Select extends Input {

  private final org.openqa.selenium.support.ui.Select select_element;
  private final boolean multi_select;
  private final HashMap<String, String> select_values;

  public Select(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.SELECT_INPUT);
    this.select_element = new org.openqa.selenium.support.ui.Select(this.getWebElement());
    this.multi_select = isMultipleSelect();
    this.select_values = new HashMap();
    this.storeSelectValues();
  }
  private boolean isMultipleSelect() {
    return this.getWebElement().getAttribute("multiple") != null;
  }

  /**
   * Select the option with the text matching the value attr.
   * @param s
   */
  @Override
  public void fill(String s) {
    this.select_element.selectByVisibleText(s); // TODO using default value in select
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
      this.select_element.selectByValue(val);
    }
  }

  /**
   * Selects the option that displays the text matching the parameter.
   *
   * @param text
   */
  private void fillByVisibleText(String text) {
    this.select_element.deselectByVisibleText(text);
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
      this.select_element.deselectByVisibleText(text);
    }
  }

  /**
   * Using select dom creates a HashMap of value and label
   */
  private void storeSelectValues() {
    WebElement select = this.getWebElement();
    List<WebElement> options = select.findElements(By.tagName("option"));
    for (WebElement option : options) {
      this.select_values.put(option.getAttribute("value"), option.getText());
    }
  }

  public boolean isMultiSelect() {
    return this.multi_select;
  }
}
