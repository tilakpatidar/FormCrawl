/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import form.ml.InputClassifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

/**
 * abstract class for all types of inputs.
 *
 * @author tilak
 */
public abstract class Input {

	private final Element input;
	private final Form form;
	private final Input.FIELDTYPES INPUT_TYPE;
	private final String input_title;
	private final String placeholder;
	private final Input.ORIENTATIONS input_orientation;
	private final Input.CATEGORIES category;
	private final String name;
	private final boolean required;

	private static final Logger LOGGER;
	private static InputClassifier CLASSIFIER;

	private static final String TOP_LABEL_IMAGE = "./img/top.png";
	private static final String LEFT_LABEL_IMAGE = "./img/left.png";
	public static final List<String> VALID_INPUT_TAGS;

	public static enum FIELDTYPES {
		TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT, FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, UNDEFINED_INPUT, RESET_BUTTON, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT, SUBMIT_INPUT
	};

	public static enum CATEGORIES {
		EMAIL, JOB_SEARCH, NAME, AGE, DOB, GENDER, DESC, PASSWORD, STATE, CITY, NATIONALITY, COUNTRY, CONTACT, ADDRESS, PIN_CODE, USERNAME, T_AND_C
	};

	public static enum ORIENTATIONS {
		LABEL_TOP, LABEL_LEFT, LABEL_RIGHT
	};

	public Input(Form f, Element ip, Input.FIELDTYPES field_type) throws IOException, Exception {
		this.INPUT_TYPE = field_type;
		this.input = ip;
		this.form = f;
		//detect input_orientation
		this.input_orientation = Input.findOrientation(this);
		this.input_title = Input.findLabel(this);
		this.placeholder = Input.findPlaceHolder(this);
		this.required = Input.findRequired(this.input);
		this.category = Input.findCategory(this.input_title + " " + this.placeholder);
		this.name = Input.findName(this);
	}

	static {
		String[] a = {"input", "textarea", "select", "button"};
		VALID_INPUT_TAGS = Arrays.asList(a);

		LOGGER = Logger.getGlobal();
		try {
			CLASSIFIER = new InputClassifier();
		} catch (Exception ex) {
			LOGGER.log(Level.INFO, "[ERROR] Unable to init InputClassifier for Input {0}", ex);
		}

	}

	public Form getAssociatedForm() {
		return this.form;
	}

	public FIELDTYPES getType() {
		return this.INPUT_TYPE;
	}

	/**
	 * Return jsoup Element of Input
	 *
	 * @return Element
	 */
	public Element getElement() {
		return this.input;
	}

	/**
	 *
	 * For getting the category of the input for suggestion engine
	 *
	 * @return
	 */
	public Input.CATEGORIES getCategory() {
		return this.category;
	}

	/**
	 * Should return title of Input
	 *
	 * @return
	 */
	public String getTitle() {
		return this.input_title;
	}

	/**
	 * Returns placeholder for element
	 *
	 * @return
	 */
	public String getPlaceHolder() {
		return this.placeholder;
	}

	public ORIENTATIONS getOrientation() {
		return this.input_orientation;
	}

	public boolean isRequired() {
		return this.required;
	}

	public String getName() {
		return this.name;
	}

	/**
	 *
	 * Fill the Input IS-A object with some value
	 */
	public abstract void fill();

	public static boolean findRequired(Element text_input) {
		return text_input.hasAttr("required");
	}

	public static String findPlaceHolder(Input input) {
		if (input.getType().equals(Input.FIELDTYPES.TEXT_INPUT) || input.getType().equals(Input.FIELDTYPES.TEXTAREA_INPUT)) {
			return input.getElement().attr("placeholder");

		} else {
			return null;
		}
	}

	public static Input.CATEGORIES findCategory(String text) throws Exception {
		return Input.CLASSIFIER.getCategory(text);
	}

	/**
	 * Get the label of the Input
	 *
	 * @param input
	 * @return
	 */
	public static String findLabel(Input input) throws IOException {
		String[] form_tokens = input.getAssociatedForm().getFormTokens();
		switch (input.getOrientation()) {
			case LABEL_TOP:
				String fn = input.getAssociatedForm().getAssociatedPage().getTopLabelScreenshot(input);
				String output = Input.execCmd("tesseract " + fn + " stdout");
				output = Input.filter_label(output);
				output = Input.correctOCRtext(output, form_tokens);
				return output;
			case LABEL_LEFT:
				String fn1 = input.getAssociatedForm().getAssociatedPage().getLeftLabelScreenshot(input);

				String output1 = Input.execCmd("tesseract " + fn1 + " stdout");
				output1 = Input.filter_label(output1);
				output1 = Input.correctOCRtext(output1, form_tokens);
				System.out.println(fn1 + "  " + output1);
				return output1;
		}
		return null;

	}

	private static String findName(Input inp) {
		return inp.getElement().attr("name");

	}

	/**
	 * Correct OCR output by using min edit distance against keywords from
	 * form
	 *
	 * @param ocr
	 * @return
	 */
	public static String correctOCRtext(String ocr, String[] form_tokens) {
		//System.out.println(ocr);
		LOGGER.log(Level.INFO, "[START] OCR Correction on {0}", ocr);
		String[] keywords = form_tokens;
		String token = ocr;
		int min = ocr.length();
		for (String keyword : keywords) {
			//System.out.println(keyword);
			int dist = StringUtils.getLevenshteinDistance(ocr, keyword);
			//System.out.println(dist);
			if (dist < min) {
				min = dist;
				token = keyword;
			}
		}
		//System.out.println(token);

		LOGGER.log(Level.INFO, "[DONE] OCR Correction on {0} - > {1}", new Object[]{ocr, token});

		return token;

	}

	public static Input.ORIENTATIONS findOrientation(Input inp) throws IOException {

		//System.out.println("HEY");
		//case 1 top label
		String file_name = inp.getAssociatedForm().getAssociatedPage().getTopLabelFieldScreenshot(inp);
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

		//throw new java.io.IOException("Unable to detect any input orientation");
	}

	public static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
		if (s.hasNext()) {
			return s.next();
		} else {
			throw new java.io.IOException("output of execCmd is empty string\nCheck your js script");
		}
	}

	/**
	 * Replaces common label punctuations used in the field title
	 *
	 * @param text
	 * @return String
	 */
	public static String filter_label(String text) {
		text = StringUtils.stripAccents(text);
		text = text.replaceAll("\\*|\\:", " ").replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ").toLowerCase();
		return text.trim();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
