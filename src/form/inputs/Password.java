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
 * Password input implementation
 *
 * @author tilak
 */
public class Password extends Input {

  public Password(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.PASSWORD_INPUT);
  }

  @Override
  public void fill(String s) {
    this.fillText("demo"); //TODO Using demo value in fillText()
  }

  private void fillText(String value) {
    WebElement e = this.getWebElement();
    e.sendKeys(value);
  }
}
