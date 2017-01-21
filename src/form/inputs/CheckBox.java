/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebElement;

import java.io.IOException;

/**
 * CheckBox input implementation
 *
 * @author tilak
 */
public class CheckBox extends Groupable {

  public CheckBox(Form f, Element ip) throws IOException {
    super(f, ip, Input.FIELDTYPES.CHECKBOX_INPUT);
  }

  @Override
  public void fill() {
    this.clickCheckbox();
  }

  private void clickCheckbox() {
    WebElement e = this.getWebElement();
    e.click();
  }
}
