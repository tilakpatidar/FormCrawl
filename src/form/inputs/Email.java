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
 * Email input implementation
 *
 * @author tilak
 */
public class Email extends Input {

  public Email(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.EMAIL_INPUT);
  }

  @Override
  public void fill(String s) {
    this.fillText("test"); // TODO Using demo value in fillText()
  }

  private void fillText(String val) {
    WebElement e = this.getWebElement();
    e.sendKeys(val);
  }
}
