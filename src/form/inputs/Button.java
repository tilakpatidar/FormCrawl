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

/**
 * @author tilak
 */
public class Button extends Input {

  private Button.TYPES button_type;

  public Button(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.BUTTON_INPUT);
  }

  ;
  @Override
  public void fill(String s) {
    this.clickButton();
  }
  @Override
  public boolean isBounded() {
    return false;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }
  private void clickButton() {
    WElement e = this.getWElement();
    e.click();
  }
  public Button.TYPES getButtonType() {
    return this.button_type;
  }
  public void setButtonType(Button.TYPES type) {
    this.button_type = type;
  }
  public void submit() {
    if (this.button_type.equals(Button.TYPES.SUBMIT)) {
      this.getWElement().click();
    } else {
      throw new UnsupportedOperationException("Only SUMBIT type Button can do SUBMIT");
    }
  }

  public static enum TYPES {
    NORMAL, SUBMIT, RESET
  }
}
