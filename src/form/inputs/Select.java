/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;

/**
 * Select input implementation
 *
 * @author tilak
 */
public class Select extends Input {

  private final org.openqa.selenium.support.ui.Select select_element;
  private final boolean multi_select;
  private final HashMap<String, String> select_values;

  public Select(Form f, Element ip) throws IOException, Exception {
    super(f, ip, Input.FIELDTYPES.SELECT_INPUT);
    this.select_element = new org.openqa.selenium.support.ui.Select(this.getWebElement());
    this.multi_select = this.getElement().hasAttr("multiple");
    this.select_values = new HashMap();
    this.storeSelectValues();
  }

  /**
   * Select the option with the text matching the value attr.
   * @param s
   */
  @Override
  public void fill(String s) {
    WebElement e = this.getWebElement();
    this.select_element.selectByValue("sdfs"); // TODO using default value in select
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
    Element select = this.getElement();
    Elements options = select.getElementsByTag("option");
    for (Element option : options) {
      this.select_values.put(option.attr("value"), option.text());
    }
  }

  public boolean isMultiSelect() {
    return this.multi_select;
  }
}
