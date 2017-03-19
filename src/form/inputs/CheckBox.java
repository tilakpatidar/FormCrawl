/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import org.openqa.selenium.WebElement;

import java.io.IOException;

/**
 * CheckBox input implementation
 *
 * @author tilak
 */
public class CheckBox extends Groupable {

  public CheckBox(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.CHECKBOX_INPUT);
  }

  @Override
  public void fill(String s) {
    this.clickCheckbox();
  }

  private void clickCheckbox() {
    try{
      WebElement e = this.getWebElement();
      e.click();
    }catch (Exception e){
      System.out.println(this.getWebElement().getLocation());
    }

  }
}
