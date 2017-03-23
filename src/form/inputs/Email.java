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
 * Email input implementation
 *
 * @author tilak
 */
public class Email extends Input {

  public Email(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.EMAIL_INPUT);
    super.placeholder = getAttr(ip, "placeholder");
  }

  @Override
  public void fill(String s) {
    this.fillText("test"); // TODO Using demo value in fillText()
  }
  @Override
  public boolean isBounded() {
    return false;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }

  private void fillText(String val) {
    WElement e = this.getWElement();
    e.sendKeys(val);
  }
}
