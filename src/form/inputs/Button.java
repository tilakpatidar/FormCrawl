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
 *
 * @author tilak
 */
public class Button extends Input {
	
	
	private Button.TYPES button_type;
	
	public static enum TYPES {
		NORMAL, SUBMIT, RESET
	};
	
	public Button(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.BUTTON_INPUT);
	}

	@Override
	public void fill(String val) {
		WebElement e = this.getWebElement();
		e.click();
	}
	
	public void setButtonType(Button.TYPES type){
		this.button_type = type;
	}
	
	public Button.TYPES getButtonType(){
		return this.button_type;
	}
	
	@Override
	protected Input.ORIENTATIONS findOrientation() throws IOException{
		return Input.ORIENTATIONS.NO_ORIENTATION_REQ;
	}
	
	public void submit(){
		if(this.button_type.equals(Button.TYPES.SUBMIT)){
			this.getWebElement().click();
		}else{
			throw new UnsupportedOperationException("Only SUMBIT type Button can do SUBMIT");
		}
	}
	
	
	
	
}
