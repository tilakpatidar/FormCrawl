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

import static form.util.SeleniumUtil.getAttr;

/**
 * Password input implementation
 *
 * @author tilak
 */
public class Password extends Input {

  public Password(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.PASSWORD_INPUT);
    super.placeholder = getAttr(ip, "placeholder");
  }

  @Override
  public void fill(String s) {
    this.fillText("demo"); //TODO Using demo value in fillText()
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
    e.sendKeys(value);
  }
}
