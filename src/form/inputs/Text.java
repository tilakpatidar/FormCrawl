/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import form.util.WElement;

import java.io.IOException;

import static form.Input.FIELD_TYPES.TEXT_INPUT;
import static form.util.SeleniumUtil.getAttr;

/**
 * Text input implementation
 *
 * @author tilak
 */
public class Text extends Input {

  public Text(Form form, WElement element) throws IOException {
    super(form, element, TEXT_INPUT);
    super.placeholder = getAttr(element, "placeholder");
  }

  @Override
  public void fill(String value) {
    this.fillText(value);
  }
  @Override
  public boolean isBounded() {
    return false;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }

  private void fillText(String value) {
    WElement e = this.getWElement();
    e.click();
    e.clear();
    e.sendKeys(value);
  }
}
