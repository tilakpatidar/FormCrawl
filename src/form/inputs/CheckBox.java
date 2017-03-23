/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.util.WElement;

import java.io.IOException;

/**
 * CheckBox input implementation
 *
 * @author tilak
 */
public class CheckBox extends Groupable {

  public CheckBox(Form f, WElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.CHECKBOX_INPUT);
  }

  @Override
  public void fill(String s) {
    this.clickCheckbox();
  }
  @Override
  public boolean isBounded() {
    return true;
  }
  @Override
  public boolean isGroupAble() {
    return true;
  }

  private void clickCheckbox() {
    try{
      WElement e = this.getWElement();
      e.click();
    }catch (Exception e){
      System.out.println(this.getWElement().getLocation());
    }

  }
}
