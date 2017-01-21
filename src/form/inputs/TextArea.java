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
 * TextArea input implementation
 *
 * @author tilak
 */
public class TextArea extends Input {

  public TextArea(Form f, Element ip) throws IOException {
    super(f, ip, Input.FIELDTYPES.TEXTAREA_INPUT);
  }

  @Override
  public void fill() {
    WebElement e = this.getWebElement();
    e.sendKeys("demo"); // TODO using demo value in TextArea
  }
}

