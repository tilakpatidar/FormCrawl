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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
/**
 * Text input implementation
 *
 * @author tilak
 */
public class Text extends Input {

	public Text(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.TEXT_INPUT);
		
	}

	@Override
	public void fill() {
		this.fillText("demo"); //TODO Using ex value in fillText()
		//System.out.println(e.getSize().getHeight() + "" + value);
	}

	private void fillText(String value){
		WebElement e = this.getWebElement();
		e.click();
		e.clear();
		e.sendKeys(value);
	}

}
