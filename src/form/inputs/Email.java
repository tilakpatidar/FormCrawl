/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebElement;
/**
 * Email input implementation
 *
 * @author tilak
 */
public class Email extends Input {

	public Email(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.EMAIL_INPUT);
	}

	@Override
	public void fill() {
		this.fillText("test"); // TODO Using demo value in fillText()
	}

	private void fillText(String val){
		WebElement e = this.getWebElement();
		e.sendKeys(val);
	}


}
