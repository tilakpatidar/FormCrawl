/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import form.inputs.Text;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Represents a form instance with many inputs and actions.
 *
 * @author tilak
 */
public class Form {

	public static enum METHODS {
		GET, POST, PUT, DELETE
	};

	private final URL form_action;
	private final Page page;

	private final Form.METHODS form_method;
	private final Element form_dom;
	private ArrayList<Input> form_inputs;
	private final String[] form_tokens;

	/**
	 * Accepts JSOUP form element
	 *
	 * @param page
	 * @param form_dom - JSOUP form element
	 */
	public Form(Page page, Element form_dom) {

		LOGGER.info("[START] Creating instance of Form");
		this.checkValidFormDom(form_dom);
		
	
		
		this.page = page;
		this.form_dom = form_dom;
		this.form_method = this.extractFormMethod();
		this.form_action = this.extractFormAction();
		
			
		String form_text = Input.filter_label(this.form_dom.text());
		String[] keywords = StringUtils.splitPreserveAllTokens(form_text);
		
		int index = 0;
		for(String keyword : keywords){
			//System.out.println(keyword);
			keywords[index] = Input.filter_label(keyword);
			index++;
		}
		
		this.form_tokens = keywords;

		LOGGER.log(Level.INFO, "[DONE] Form action and method detected");

		Elements input_collection = Form.detectFields(this.form_dom);
		this.form_inputs = new ArrayList<Input>();

		LOGGER.log(Level.INFO, "[DONE] Found {0} inputs", input_collection.size());

		int count = 1;
		for (Element input : input_collection) {
			try {
				LOGGER.log(Level.INFO, "[START] Create Input instance {0}", count);
				Input input_obj = this.detectInput(input);
				this.form_inputs.add(input_obj);
				LOGGER.log(Level.INFO, "[DONE] Created Input instance {0}", count);
			} catch (Exception e) {
				LOGGER.log(Level.INFO, "[FAIL] Create Input instance {0}", count);
				e.printStackTrace();
			}
			count += 1;
		}
		LOGGER.log(Level.INFO, "[DONE] Created {0} Input objects", form_inputs.size());
		//System.out.println("LL515");

		LOGGER.info(this.toString());

	}

	//for logging in class Form
	private static final Logger LOGGER;

	static {
		LOGGER = Logger.getGlobal();
	}
	
	public String[] getFormTokens(){
		return this.form_tokens;
	}

	private void checkValidFormDom(Element form_dom) {
		//check for type before construction
		if (!form_dom.tagName().equalsIgnoreCase("FORM")) {
			LOGGER.info("[ERROR] form_dom must be of tagName type \"form\"");
			LOGGER.log(Level.INFO, "[FAIL] Creating instance of Form failed");
			throw new IllegalArgumentException("form_dom must be of tagName type \"form\"");
		}
	}

	/**
	 * Extracts and returns form method
	 *
	 * @return
	 */
	private Form.METHODS extractFormMethod() {
		String method = this.form_dom.attr("method").toUpperCase();

		LOGGER.log(Level.INFO, "[INFO] Form method type {0}", method);

		switch (method) {
			case "GET":
				return Form.METHODS.GET;
			case "POST":
				return Form.METHODS.POST;
			case "PUT":
				return Form.METHODS.PUT;
			case "DELETE":
				return Form.METHODS.DELETE;
			default:
				//no matching method
				return Form.METHODS.GET;
			//The default method when submitting form data is GET. (from w3schools)

		}
	}

	/**
	 * Extract and returns form action
	 *
	 * @return
	 */
	private URL extractFormAction() {
		try {
			URI orig_form_url = new URI(this.form_dom.attr("action"));

			if (orig_form_url.toString().isEmpty()) {
				LOGGER.log(Level.INFO, "[ERROR] No form action url present in form_dom");
				throw new MalformedURLException("URL empty or null");
			}

			if (!orig_form_url.isAbsolute()) {
				//make absolute
				return new URL(page.url_value, orig_form_url.toString());
			} else {
				return orig_form_url.toURL();
			}
		} catch (MalformedURLException ex) {
			LOGGER.log(Level.INFO, "[ERROR] FORM action URL malformed {0}", ex.getMessage());
			LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
			throw new RuntimeException("Cannot create form object");
		} catch (URISyntaxException ex) {
			LOGGER.log(Level.INFO, "[ERROR] FORM action URI cast malformed {0}", ex.getMessage());
			LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
			throw new RuntimeException("Cannot create form object");
		}
	}

	public static Elements detectFields(Element form_dom) {

		Elements input_collection = new Elements();
		for (String tag : Input.VALID_INPUT_TAGS) {
			input_collection.addAll(form_dom.getElementsByTag(tag));
		}

		Elements form_elements = new Elements();

		for (Element e : input_collection) {

			//not hidden element and element is part of valid input tags 
			if (!e.attr("type").toLowerCase().equals("hidden") && Input.VALID_INPUT_TAGS.contains(e.tagName())) {
				form_elements.add(e);
			}

		}
		return form_elements;
	}

	protected Input detectInput(Element ip) throws IOException {
		String tag_name = ip.tagName();
		Input inp;
		switch (tag_name) {
			case "input":
				String input_type = ip.attr("type").toLowerCase();
				Input.FIELDTYPES field_type;
				switch (input_type) {
					case "text":
						field_type = Input.FIELDTYPES.TEXT_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "radio":
						field_type = Input.FIELDTYPES.RADIO_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "file":
						field_type = Input.FIELDTYPES.FILE_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "checkbox":
						field_type = Input.FIELDTYPES.CHECKBOX_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "button":
						field_type = Input.FIELDTYPES.BUTTON_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "color":
						field_type = Input.FIELDTYPES.COLOR_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "date":
						field_type = Input.FIELDTYPES.DATE_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "email":
						field_type = Input.FIELDTYPES.EMAIL_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "hidden":
						field_type = Input.FIELDTYPES.HIDDEN_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "image":
						field_type = Input.FIELDTYPES.IMAGE_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "number":
						field_type = Input.FIELDTYPES.NUMBER_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "password":
						field_type = Input.FIELDTYPES.PASSWORD_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "reset":
						field_type = Input.FIELDTYPES.RESET_BUTTON;
						inp = new Text(this, ip, field_type);
						break;
					case "search":
						field_type = Input.FIELDTYPES.SEARCH_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "submit":
						field_type = Input.FIELDTYPES.SUBMIT_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "tel":
						field_type = Input.FIELDTYPES.TELEPHONE_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "time":
						field_type = Input.FIELDTYPES.TIME_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "url":
						field_type = Input.FIELDTYPES.URL_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					case "week":
						field_type = Input.FIELDTYPES.WEEK_INPUT;
						inp = new Text(this, ip, field_type);
						break;
					default:
						field_type = Input.FIELDTYPES.UNDEFINED_INPUT;
						throw new RuntimeException("Undefined input detected");
				}
				break;

			case "select":
				field_type = Input.FIELDTYPES.SELECT_INPUT;
				inp = new Text(this, ip, field_type);
				break;
			case "button":
				field_type = Input.FIELDTYPES.BUTTON_INPUT;
				inp = new Text(this, ip, field_type);
				break;
			case "textarea":
				field_type = Input.FIELDTYPES.TEXTAREA_INPUT;
				inp = new Text(this, ip, field_type);
				break;
			default:
				field_type = Input.FIELDTYPES.UNDEFINED_INPUT;
				inp = new Text(this, ip, field_type);
				throw new RuntimeException("Undefined input detected");
		}

		return inp;
	}

	public Page getAssociatedPage() {
		return this.page;
	}

	public Element getElement() {
		return this.form_dom;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
