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
 * TextArea input implementation
 *
 * @author tilak
 */
public class TextArea extends Input {

  public TextArea(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.TEXTAREA_INPUT);
    super.placeholder = getAttr(ip, "placeholder");
  }

  @Override
  public void fill(String s) {
    WElement e = this.getWElement();
    e.sendKeys("demo"); // TODO using demo value in TextArea
  }
  @Override
  public boolean isBounded() {
    return false;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }
}

