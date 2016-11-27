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
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Represents a form instance with many inputs and actions.
 *
 * @author tilak
 */
public class Form {

	

	public static enum METHODS {
		GET, POST, PUT, DELETE
	};

	public static enum ORIENTATIONS {
		LABEL_TOP, LABEL_LEFT, HTML_LAYOUT_TREE, MULTIPLE_LABELS_SPAN
	};
	private final URL form_action;
	private final Page page;
	private Form.ORIENTATIONS form_orientation;
	private final Form.METHODS form_method;
	private final Element form_dom;
	private ArrayList<Input> form_inputs;

	/**
	 * Accepts JSOUP form element
	 *
	 * @param form_dom - JSOUP form element
	 */
	public Form(Page page, Element form_dom) {

		LOGGER.info("[INFO] Creating instance of Form");
		//check for type before construction
		if (!form_dom.tagName().equalsIgnoreCase("FORM")) {
			LOGGER.info("[ERROR] form_dom must be of tagName type \"form\"");
			throw new RuntimeException("form_dom must be of tagName type \"form\"");
		}

		String method = form_dom.attr("method").toUpperCase();
		switch (method) {
			case "GET":
				this.form_method = Form.METHODS.GET;
				break;
			case "POST":
				this.form_method = Form.METHODS.POST;
				break;
			case "PUT":
				this.form_method = Form.METHODS.PUT;
				break;
			case "DELETE":
				this.form_method = Form.METHODS.DELETE;
				break;
			default:
				//no matching method
				this.form_method = Form.METHODS.GET;
			//The default method when submitting form data is GET. (from w3schools)

		}

		try {
			URI form_action = new URI(form_dom.attr("action"));
			if (form_action.toString().isEmpty()) {
				LOGGER.info("[ERROR] No form action url present in form_dom");
				throw new MalformedURLException("URL empty or null");
			}

			if (!form_action.isAbsolute()) {
				//make absolute
				this.form_action = new URL(page.url_value, form_action.toString());
			} else {
				this.form_action = form_action.toURL();
			}
		} catch (MalformedURLException ex) {
			LOGGER.info("[ERROR] FORM action URL malformed");
			LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
			throw new RuntimeException("Cannot create form object");
		} catch (URISyntaxException ex) {
			LOGGER.info("[ERROR] FORM action URI cast malformed");
			LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
			throw new RuntimeException("Cannot create form object");
		}

		
		
		this.page = page;
		this.form_dom = form_dom;
		this.form_inputs = new ArrayList<Input>();
		
		//detect form_orientation
		System.out.println("LL5");
		try{
			this.form_orientation = this.detectOrientation();
		}catch(Exception e){
			e.printStackTrace();
		}
		
System.out.println("LL55");
		LOGGER.info("[INFO] Form action and method detected");

		Elements input_collection = Form.getInputElements(Form.detectFields(this.form_dom));
System.out.println("LL5555");
		for (Element input : input_collection) {
			try {
			Input input_obj = this.detectInput(input);
			this.form_inputs.add(input_obj);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		LOGGER.log(Level.INFO, "[INFO] Created {0}Input objects", form_inputs.size());
		System.out.println("LL515");
		
		try {
			System.out.println("LL");
			this.page.getTopLabelScreenshot(form_inputs.get(0));
			System.out.println("LL2");
			this.page.getTopLabelScreenshot(form_inputs.get(1));
			this.page.getTopLabelScreenshot(form_inputs.get(2));
		} catch (IOException ex) {
			ex.printStackTrace();
			LOGGER.log(Level.FINEST, null, ex);
		}catch (Exception e){
			
			e.printStackTrace();
		}

		LOGGER.info(this.toString());

	}

	//for logging in class Form
	private static final Logger LOGGER;

	static {
		LOGGER = Logger.getGlobal();
	}

	public static Elements detectFields(Element form_dom) {

		Elements input_collection = form_dom.getElementsByTag("input");
		Elements select_collection = form_dom.getElementsByTag("select");
		Elements button_collection = form_dom.getElementsByTag("button");
		Elements textarea_collection = form_dom.getElementsByTag("textarea");

		input_collection.addAll(select_collection);
		input_collection.addAll(button_collection);
		input_collection.addAll(textarea_collection);

		return input_collection;
	}

	protected Input detectInput(Element ip) {
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

	public Form.ORIENTATIONS getOrientation() {
		return this.form_orientation;
	}

	private Form.ORIENTATIONS detectOrientation() {
		//take any first input
		Element input = this.form_dom.getElementsByTag("input").first();
		Element parent = input.parent();
		System.out.println("HEYYE78");
		if (Form.detectFields(parent).size() == 1) {
			//it's parent have only one input
			//now try if it's parent's parent's have multiple inputs
			if (Form.detectFields(parent.parent()).size() > 1) {
				//yes it has div by div structure
				return Form.ORIENTATIONS.HTML_LAYOUT_TREE;
			}
		} else {
			System.out.println("HEYYefE");
			//case 1 : if label is next or prev of input
			Element next = input.nextElementSibling();
			Element prev = input.previousElementSibling();
			if (next != null && (next.tagName().equals("label") || next.tagName().equals("span")) ) {
				return Form.ORIENTATIONS.MULTIPLE_LABELS_SPAN;
			} else if (prev != null && (prev.tagName().equals("label") || prev.tagName().equals("span") )) {
				return Form.ORIENTATIONS.MULTIPLE_LABELS_SPAN;
			} else {
				System.out.println("HEYYE");
				try {
					//try for image comparision
					//try for label at top and input below
					System.out.println("LLAL1");
					//this.page.getFieldScreenshot(input);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return Form.ORIENTATIONS.HTML_LAYOUT_TREE;
	}
	
	public Element getElement(){
		return this.form_dom;
	}
	
	/**
	 * Takes an array list of Element and returns only form input elements within it.
	 * @param children
	 * @return 
	 */
	public static Elements getInputElements(Elements children) {
		ArrayList<String> inputs = new ArrayList<String>();
		inputs.add("input");
		inputs.add("select");
		inputs.add("textarea");
		
		Elements form_elements = new Elements();
		for(Element e : children){
			
			if( !e.attr("type").toLowerCase().equals("hidden") && inputs.indexOf(e.tagName()) >= 0 ){
				form_elements.add(e);
			}
			
		}
		return form_elements;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
