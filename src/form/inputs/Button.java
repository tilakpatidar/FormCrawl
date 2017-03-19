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
 * @author tilak
 */
public class Button extends Input {

  private Button.TYPES button_type;

  public Button(Form f, WebElement ip) throws IOException {
    super(f, ip, FIELD_TYPES.BUTTON_INPUT);
  }

  ;
  @Override
  public void fill(String s) {
    this.clickButton();
  }
  private void clickButton() {
    WebElement e = this.getWebElement();
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
      this.getWebElement().click();
    } else {
      throw new UnsupportedOperationException("Only SUMBIT type Button can do SUBMIT");
    }
  }

  public static enum TYPES {
    NORMAL, SUBMIT, RESET
  }
}
