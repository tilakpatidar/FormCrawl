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
 * Select input implementation
 *
 * @author tilak
 */
public class Select extends Input {

	private final org.openqa.selenium.support.ui.Select select_element;
	private final boolean multi_select;

	public Select(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.SELECT_INPUT);
		this.select_element = new org.openqa.selenium.support.ui.Select(this.getWebElement());
		this.multi_select = this.getElement().hasAttr("multiple");
	}

	/**
	 * Select the option with the text matching the value attr. 
	 *
	 * @param val
	 */
	@Override
	public void fill(String val) {
		WebElement e = this.getWebElement();
		this.select_element.selectByValue(val);

	}

	/**
	 * Selects the options with the text matching the value attr. Only for
	 * multiple attr selects
	 *
	 * @param vals
	 */
	public void fill(String[] vals) throws UnsupportedOperationException {
		if (!this.isMultiSelect()) {
			throw new UnsupportedOperationException("Not supported for non multiple select");
		}
		for (String val : vals) {
			this.select_element.selectByValue(val);
		}
	}

	/**
	 * Selects the option that displays the text matching the parameter.
	 *
	 * @param text
	 */
	public void fillByVisibleText(String text) {
		this.select_element.deselectByVisibleText(text);
	}

	/**
	 * Selects the options that displays the text matching the parameters.
	 * Only for multiple attr selects
	 *
	 * @param texts
	 */
	public void fillByVisibleText(String[] texts) throws UnsupportedOperationException {
		if (!this.isMultiSelect()) {
			throw new UnsupportedOperationException("Not supported for non multiple select");
		}
		for (String text : texts) {
			this.select_element.deselectByVisibleText(text);
		}
	}

	public boolean isMultiSelect() {
		return this.multi_select;
	}

}
