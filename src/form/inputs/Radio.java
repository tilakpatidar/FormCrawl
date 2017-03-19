/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import org.openqa.selenium.WebElement;

import java.io.IOException;

/**
 * Radio input implementation
 *
 * @author tilak
 */
public class Radio extends Input {

  public Radio(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.RADIO_INPUT);
  }


  @Override
  public void fill(String s) {
    this.clickRadio();
  }

  private void clickRadio() {
    WebElement e = this.getWebElement();
    e.click();
  }
}
