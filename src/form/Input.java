/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import form.ml.InputClassifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * abstract class for all types of inputs.
 *
 * @author tilak
 */
public abstract class Input {

  public static final List<String> VALID_INPUT_TAGS;
  protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  protected static final String TOP_LABEL_IMAGE = "./img/top.png";
  protected static final String LEFT_LABEL_TEXT_IMAGE = "./img/left.png";
  private static InputClassifier CLASSIFIER;

  static {
    String[] a = {"input", "textarea", "select", "button"};
    VALID_INPUT_TAGS = Arrays.asList(a);

    try {
      CLASSIFIER = new InputClassifier();
    } catch (Exception ex) {
      LOGGER.log(Level.INFO, "[ERROR] Unable to init InputClassifier for Input");
      LOGGER.log(Level.FINEST, "[ERROR]", ex);
    }
  }

  private final Element input;
  private final Form form;
  private final Input.FIELDTYPES INPUT_TYPE;
  private final String input_title;
  private final String placeholder;
  private final Input.ORIENTATIONS input_orientation;
  private final Input.CATEGORIES category;
  private final String name;
  private final boolean required;
  private final WebElement web_element;
  private final String css_selector;

  ;

  public Input(Form f, Element ip, Input.FIELDTYPES field_type) throws IOException {
    this.INPUT_TYPE = field_type;
    this.input = ip;
    this.form = f;

    this.input_orientation = this.findOrientation();

    this.input_title = this.findLabel();

    this.placeholder = this.findPlaceHolder();

    this.required = Input.findRequired(this.input);
    this.category = this.findCategory(this.input_title + " " + this.placeholder);
    this.name = Input.findName(this);
    this.css_selector = this.input.cssSelector();
    this.web_element = this.getAssociatedForm().getAssociatedPage().getDriver().findElement(By.cssSelector(this.css_selector));
  }

  ;
  public static boolean findRequired(Element text_input) {
    return text_input.hasAttr("required");
  }

  ;
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
    LOGGER.log(Level.INFO, "[START] OCR Correction on {0}", ocr);
    String[] keywords = form_tokens;
    String token = ocr;
    int min = ocr.length();
    for (String keyword : keywords) {
      int dist = StringUtils.getLevenshteinDistance(ocr, keyword);
      if (dist < min) {
        min = dist;
        token = keyword;
      }
    }

    LOGGER.log(Level.INFO, "[DONE] OCR Correction on {0} - > {1}", new Object[]{ocr, token});

    return token;
  }
  public static String execCmd(String cmd) throws java.io.IOException {
    java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
    if (s.hasNext()) {
      return s.next();
    } else {
      LOGGER.log(Level.FINEST, "[ERROR] output of execCmd is empty string. Check your js script");
      throw new java.io.IOException("output of execCmd is empty string. Check your js script");
    }
  }
  /**
   * Replaces common label punctuations used in the field title and strip Accents
   *
   * @param text
   * @return String
   */
  public static String filter_label(String text) {
    text = StringUtils.stripAccents(text);
    text = text.replaceAll("\\*|\\:", " ").replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ").toLowerCase();
    return text.trim();
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
   * Return WebElement of Input
   *
   * @return WebElement
   */
  public WebElement getWebElement() {
    return this.web_element;
  }
  /**
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
  public String getTooltipData() {

    String data = "Input label - " + this.input_title + "<br/>Input category - " + this.getCategory().toString() + "<br/>Input orientation - " + this.getOrientation().toString();
    return data;
  }
  public String getCSSSelector() {
    return this.css_selector;
  }
  /**
   * Available in all Input types, to fill data.
   * Each input type overrides this and implement their own filling logic.
   */
  public abstract void fill();
  private String findPlaceHolder() {
    if (this.getType().equals(Input.FIELDTYPES.TEXT_INPUT) || this.getType().equals(Input.FIELDTYPES.TEXTAREA_INPUT)) {
      return this.getElement().attr("placeholder");
    } else {
      return "";
    }
  }
  private Input.CATEGORIES findCategory(String text) {
    if (this.getType().equals(Input.FIELDTYPES.BUTTON_INPUT)) {
      return Input.CATEGORIES.ACTION_ITEM;
    } else {
      return Input.CLASSIFIER.getCategory(text);
    }
  }
  /**
   * Get the label of the Input
   *
   * @return
   */
  private String findLabel() throws IOException {

    if (this.getType().equals(Input.FIELDTYPES.BUTTON_INPUT)) {
      return this.getElement().text();
    }

    //search if dev used a label[for=]
    String id = this.getElement().id();
    final Elements associated_labels = this.getAssociatedForm().getElement().select("label[for='" + id + "'");
    if (associated_labels.first() != null) {
      return Input.filter_label(associated_labels.first().text());
    }

    String[] form_tokens = this.getAssociatedForm().getFormTokens();
    switch (this.getOrientation()) {
      case LABEL_TOP:
        String fn = this.getAssociatedForm().getAssociatedPage().getTopLabelScreenshot(this);
        String output = Input.execCmd("tesseract " + fn + " stdout");
        output = Input.filter_label(output);
        output = Input.correctOCRtext(output, form_tokens);
        return output;
      case LABEL_LEFT:
        String fn1 = this.getAssociatedForm().getAssociatedPage().getLeftLabelScreenshot(this);

        String output1 = Input.execCmd("tesseract " + fn1 + " stdout");
        output1 = Input.filter_label(output1);
        output1 = Input.correctOCRtext(output1, form_tokens);
        return output1;
    }
    return null;
  }
  protected Input.ORIENTATIONS findOrientation() throws IOException {

    LOGGER.log(Level.INFO, "[INFO] Detecting orientation");
    //case 1 top label
    //detect input_orientation
    String id = this.getElement().id();
    LOGGER.log(Level.FINER, "[DEBUG] Element id " + id);
    final Elements associated_labels = this.getAssociatedForm().getElement().select("label[for='" + id + "'");
    LOGGER.log(Level.FINEST, "[DEBUG] " + associated_labels.toString());

    if (associated_labels.first() != null) {
      return ORIENTATIONS.NO_ORIENTATION_REQ; //case when devs used label for
      // tags
    }

    String file_name = this.getAssociatedForm().getAssociatedPage().getTopLabelFieldScreenshot(this);
    LOGGER.log(Level.FINER, "[DEBUG] shell command node ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);

    String output = Input.execCmd("node ./js_scripts/diff.js " + file_name + " " + Input.TOP_LABEL_IMAGE);

    output = output.replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ").trim();

    Double diff = Double.parseDouble(output);
    int diff_per = diff.intValue();

    LOGGER.log(Level.FINER, "[DEBUG] Diff value " + diff_per);

    if (diff_per < 10) {
      //yes label is on top
      return Input.ORIENTATIONS.LABEL_TOP;
    } else {
      return Input.ORIENTATIONS.LABEL_LEFT;
    }

    //throw new java.io.IOException("Unable to detect any input orientation");
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public static enum FIELDTYPES {
    TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT, FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT
  }

  public static enum CATEGORIES {
    EMAIL, JOB_SEARCH, NAME, AGE, DOB, GENDER, DESC, PASSWORD, STATE, CITY,
    NATIONALITY, COUNTRY, CONTACT, ADDRESS, PIN_CODE, USERNAME, T_AND_C,
    ACTION_ITEM, UNDEFINED
  }

  public static enum ORIENTATIONS {
    LABEL_TOP, LABEL_LEFT, LABEL_RIGHT, NO_ORIENTATION_REQ
  }
}
