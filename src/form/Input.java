/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;

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

	private static final String TOP_LABEL_IMAGE = "./img/top.png";
	private static final String LEFT_LABEL_IMAGE = "./img/left.png";
	
	public static final List<String> VALID_INPUT_TAGS;
	
	static{
		String[] a = {"input", "textarea", "select", "button" };
		VALID_INPUT_TAGS = Arrays.asList(a);
	}

	public static enum ORIENTATIONS {
		LABEL_TOP, LABEL_LEFT, LABEL_RIGHT
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
		return null;

	}

	public static Input.ORIENTATIONS detectOrientation(Input inp) throws IOException {

		System.out.println("HEY");
		//case 1 top label
		String file_name = inp.getAssociatedForm().getAssociatedPage().getTopLabelFieldScreenshot(inp);
		LOGGER.log(Level.INFO, "nodejs ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);
		String output = Input.execCmd("nodejs ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);

		output = output.replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ").trim();
		System.out.println(output);
		Double diff = Double.parseDouble(output);
		int diff_per = diff.intValue();
		if (diff_per < 10) {
			//yes label is on top
			return Input.ORIENTATIONS.LABEL_TOP;
		} else {
			return Input.ORIENTATIONS.LABEL_LEFT;
		}

		//throw new java.io.IOException("Unable to detect any input orientation");
	}

	private static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
		if (s.hasNext()) {
			return s.next();
		} else {
			throw new java.io.IOException("output of execCmd is empty string\nCheck your js script");
		}
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

	public abstract Input.ORIENTATIONS getOrientation();

	//for logging
	private static final Logger LOGGER;

	static {
		LOGGER = Logger.getGlobal();
	}
}
