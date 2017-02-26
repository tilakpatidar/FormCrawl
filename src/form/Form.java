/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import form.autofill.fillers.AutoFill;
import form.autofill.fillers.BasicAutoFill;
import form.inputs.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a form instance with many inputs and actions.
 *
 * @author tilak
 */
public class Form {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final Class<BasicAutoFill> FILL_CLASS = BasicAutoFill.class;
  private final Page page;

  private final Form.METHODS form_method;
  private final Element formDom;
  private final String[] formTokens;
  private final HashMap<String, String> params;
  private ArrayList<Input> form_inputs;
  private ArrayList<Group> inputGroups;
  private Button resetButton;
  private Button submitButton;
  /**
   * Accepts JSOUP form element
   *
   * @param page    - Page object
   * @param formDom - JSOUP form element
   */
  public Form(Page page, Element formDom) {

    LOGGER.info("[START] Creating instance of Form");
    this.checkValidFormDom(formDom);
    this.params = new HashMap<>();
    this.page = page;
    this.formDom = formDom;
    this.form_method = this.extractFormMethod();

    String form_text = Input.filter_label(this.formDom.text());
    String[] keywords = StringUtils.splitPreserveAllTokens(form_text);

    int index = 0;
    for (String keyword : keywords) {
      keywords[index] = Input.filter_label(keyword);
      index++;
    }

    this.formTokens = keywords;

    LOGGER.log(Level.INFO, "[DONE] Form action and method detected");

    //creating instances of Inputs
    Elements input_collection = Form.detectFields(this.formDom);
    this.form_inputs = new ArrayList<>();

    LOGGER.log(Level.INFO, "[DONE] Found {0} inputs", input_collection.size());

    int count = 1;
    for (Element input : input_collection) {
      try {
        LOGGER.log(Level.INFO, "[START] Create Input instance {0}", count);
        Input input_obj = this.detectInput(input);
        this.form_inputs.add(input_obj);
        LOGGER.log(Level.FINER, input_obj.toString());
        if (input_obj instanceof Button) {
          LOGGER.log(Level.FINER, "[FINER] Button instance detected");
          Button b = (Button) input_obj;
          if (b.getButtonType().equals(Button.TYPES.SUBMIT)) {
            LOGGER.log(Level.FINER, "[FINER] Button is submit");
            this.submitButton = b;
          } else if (b.getButtonType().equals(Button.TYPES.RESET)) {
            LOGGER.log(Level.FINER, "[FINER] Button is reset");
            this.resetButton = b;
          }
        }
        LOGGER.log(Level.INFO, "[DONE] Created Input instance {0}", count);
        this.page.createTooltip(input_obj.getWebElement(), input_obj.getTooltipData());
      } catch (Exception e) {
        LOGGER.log(Level.INFO, "[FAIL] Create Input instance {0}", count);
        LOGGER.log(Level.FINEST, "[ERROR]", e);
      }
      count += 1;
    }

    if (this.submitButton == null) {
      LOGGER.log(Level.INFO, "[FAIL] No submit button present abort form creation");
      throw new RuntimeException("No submit button present");
    }

    LOGGER.log(Level.INFO, "[DONE] Created {0} Input objects", form_inputs.size());

    LOGGER.log(Level.FINER, this.toString());
  }
  private static Elements detectFields(Element form_dom) {

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
  String[] getFormTokens() {
    return this.formTokens;
  }

  public ArrayList<Input> getAssociatedInputs() {
    return this.form_inputs;
  }

  void submitForm() throws Exception {
    this.fillForm(FILL_CLASS);
    Button b = this.getSubmitButton();
    b.getWebElement().click();
  }

  private Button getSubmitButton() {
    return this.submitButton;
  }

  private void fillForm(Class<? extends AutoFill> T) throws Exception {
    AutoFill filler = T.newInstance();
    filler.init(this);
    filler.fill();
  }

  private void checkValidFormDom(Element formDom) {
    //check for type before construction
    if (!formDom.tagName().equalsIgnoreCase("FORM")) {
      LOGGER.log(Level.WARNING, "[FAIL] formDom must be of tagName type \"form\"");
      throw new IllegalArgumentException("formDom must be of tagName type \"form\"");
    }
    LOGGER.info("[DONE] Dom checked for validation.");
  }

  public ArrayList<Group> getInputGroups() {
    return this.inputGroups;
  }

  /**
   * Finds the input name by name and type
   *
   * @param name - form name tag value
   * @return - Return a Group object
   */
  public Group findGroupByName(String class_name, String name) {

    for (Group i : this.inputGroups) {
      if (i.getClass().getCanonicalName().equals(class_name) && i.getName().equals(name)) {
        return i;
      }
    }

    return null;
  }

  /**
   * Extracts and returns form method
   *
   * @return - Returns form method type
   */
  private Form.METHODS extractFormMethod() {
    String method = this.formDom.attr("method").toUpperCase();

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
  private Input detectInput(Element ip) throws IOException {
    String tag_name = ip.tagName();
    Input inp;
    Button bt;
    switch (tag_name) {
      case "input":
        String input_type = ip.attr("type").toLowerCase();
        switch (input_type) {
          case "text":
            inp = new Text(this, ip);
            break;
          case "radio":
            inp = new Radio(this, ip);
            break;
          case "file":
            inp = new Text(this, ip);
            break;
          case "checkbox":
            inp = new CheckBox(this, ip);
            break;
          case "button":
            inp = new Button(this, ip);
            bt = (Button) inp;
            bt.setButtonType(Button.TYPES.NORMAL);
            break;
          case "color":
            inp = new Text(this, ip);
            break;
          case "date":
            inp = new Text(this, ip);
            break;
          case "email":
            inp = new Email(this, ip);
            break;
          case "hidden":
            inp = new Text(this, ip);
            break;
          case "image":
            inp = new Text(this, ip);
            break;
          case "number":
            inp = new Text(this, ip);
            break;
          case "password":
            inp = new Password(this, ip);
            break;
          case "reset":
            inp = new Button(this, ip);
            bt = (Button) inp;
            bt.setButtonType(Button.TYPES.RESET);
            break;
          case "search":
            inp = new Text(this, ip);
            break;
          case "submit":
            inp = new Button(this, ip);
            bt = (Button) inp;
            bt.setButtonType(Button.TYPES.SUBMIT);
            break;
          case "tel":
            inp = new Text(this, ip);
            break;
          case "time":
            inp = new Text(this, ip);
            break;
          case "url":
            inp = new Text(this, ip);
            break;
          case "week":
            inp = new Text(this, ip);
            break;
          default:
            LOGGER.log(Level.INFO, "[FAIL] Undefined input detected");
            LOGGER.log(Level.FINEST, "[ERROR] nothing in switch case matches " + input_type);
            throw new RuntimeException("Undefined input detected");
        }
        break;

      case "select":
        inp = new Text(this, ip);
        break;
      case "button":
        String in = ip.attr("type").toLowerCase();
        switch (in) {
          case "reset":
            inp = new Button(this, ip);
            bt = (Button) inp;
            bt.setButtonType(Button.TYPES.RESET);
            break;
          case "submit":
            inp = new Button(this, ip);
            try {
              bt = (Button) inp;
              bt.setButtonType(Button.TYPES.SUBMIT);
            } catch (Exception e) {
              e.printStackTrace();
            }

            break;
          default:
            inp = new Button(this, ip);
            bt = (Button) inp;
            bt.setButtonType(Button.TYPES.NORMAL);
        }
        break;

      case "textarea":
        inp = new TextArea(this, ip);
        break;
      default:
        LOGGER.log(Level.INFO, "[FAIL] Undefined input detected");
        LOGGER.log(Level.FINEST, "[ERROR] nothing in switch case matches " + tag_name);
        throw new RuntimeException("Undefined input detected");
    }

    return inp;
  }
  public Page getAssociatedPage() {
    return this.page;
  }
  public Element getElement() {
    return this.formDom;
  }
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public enum METHODS {
    GET, POST, PUT, DELETE
  }
}
