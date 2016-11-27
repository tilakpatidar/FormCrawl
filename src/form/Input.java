/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;

/**
 * abstract class for all types of inputs.
 *
 * @author tilak
 */
public abstract class Input {

	/**
	 *
	 * Tells us the various types of field types available in the HTML form
	 */
	public static enum FIELDTYPES {
		TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT, FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, UNDEFINED_INPUT, RESET_BUTTON, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT, SUBMIT_INPUT
	};

	public abstract Form getAssociatedForm();

	/**
	 * For getting the FIELD_TYPE of any Input IS-A Object
	 *
	 * @return
	 */
	public abstract Input.FIELDTYPES getType();

	/**
	 *
	 * Fill the Input IS-A object with some value
	 */
	public abstract void fill();

	/**
	 * Return jsoup Element of Input
	 *
	 * @return Element
	 */
	public abstract Element getElement();

	public static String findPlaceHolder(Input input) {
		if (input.getType().equals(Input.FIELDTYPES.TEXT_INPUT) || input.getType().equals(Input.FIELDTYPES.TEXTAREA_INPUT)) {
			return input.getElement().attr("placeholder");

		} else {
			return null;
		}
	}

	/**
	 *
	 * Returns text from image
	 *
	 * @return
	 */
	private String getImageText() {
		return null;

	}

	/**
	 * Get the label of the Input
	 *
	 * @param input
	 * @return
	 */
	public static String findLabel(Input input) {
		
		Form f = input.getAssociatedForm();
		
		switch(Form.ORIENTATIONS.HTML_LAYOUT_TREE){
			case HTML_LAYOUT_TREE:
				Element parent = input.getElement().parent();
				if (Form.detectFields(parent).size() == 1) {
					//it's parent have only one input
					//now try if it's parent's parent's have multiple inputs
					if (Form.detectFields(parent.parent()).size() > 1) {
						//yes it has div by div structure
						return Input.filter_label(parent.text());
					}
				}
			break;
			
			case MULTIPLE_LABELS_SPAN:
				Element next = input.getElement().nextElementSibling();
				Element prev = input.getElement().previousElementSibling();
				if (next.tagName().equals("label") || next.tagName().equals("span")) {
					return Input.filter_label(next.text());
				} else if (prev.tagName().equals("label") || prev.tagName().equals("span")) {
					return Input.filter_label(prev.text());
				}
				break;
			default:
				return "NO LABEL";
		}
		return null;
		
	}

	
	
	/**
	 *
	 * For getting the category of the input for suggestion engine
	 *
	 * @return
	 */
	public abstract String getCategory();

	/**
	 * Replaces common label punctuations used in the field title
	 *
	 * @param text
	 * @return String
	 */
	public static String filter_label(String text) {
		text = text.replaceAll("\\*|\\:|\\-", " ").replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ");
		return text.trim();
	}

	/**
	 * Should return title of Input
	 *
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * Returns placeholder for element
	 *
	 * @return
	 */
	public abstract String getPlaceHolder();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	//for logging
	private static final Logger LOGGER;
	static{
		LOGGER = Logger.getGlobal();
	}
}
