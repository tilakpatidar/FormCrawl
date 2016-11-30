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
 * Radio input implementation
 *
 * @author tilak
 */
public class Radio extends Input {

	
	public Radio(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.RADIO_INPUT);
	}

	@Override
	public void fill(String val) {
		WebElement e = this.getWebElement();
		e.click();
	}


}
