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
 * Password input implementation
 *
 * @author tilak
 */
public class Password extends Input {

	public Password(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.PASSWORD_INPUT);
		
	}

	@Override
	public void fill() {
		this.fillText("demo"); //TODO Using demo value in fillText()
	}

	private void fillText(String value){
		WebElement e = this.getWebElement();
		e.sendKeys(value);
	}


}
