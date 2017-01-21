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
import java.util.logging.Level;

/**
 * Radio input implementation
 *
 * @author tilak
 */
public class Radio extends Input {

  public Radio(Form f, Element ip) throws IOException {
    super(f, ip, Input.FIELDTYPES.RADIO_INPUT);
  }

  @Override
  protected Input.ORIENTATIONS findOrientation() throws IOException {
    String file_name = this.getAssociatedForm().getAssociatedPage().getLeftLabelScreenshot(this);
    LOGGER.log(Level.INFO, "nodejs ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);
    String output = Input.execCmd("nodejs ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);

    output = output.replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ").trim();
    //System.out.println(output);
    Double diff = Double.parseDouble(output);
    int diff_per = diff.intValue();
    if (diff_per < 10) {
      //yes label is on top
      return Input.ORIENTATIONS.LABEL_TOP;
    } else {
      return Input.ORIENTATIONS.LABEL_LEFT;
    }
  }

  @Override
  public void fill() {
    this.clickRadio();
  }

  private void clickRadio() {
    WebElement e = this.getWebElement();
    e.click();
  }
}
