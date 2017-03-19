package form;

import form.ml.InputClassifier;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static form.util.SeleniumUtil.getAttr;

public abstract class Input {

  static final List<String> VALID_INPUT_TAGS;
  private static InputClassifier CLASSIFIER;

  static {
    String[] a = {"input", "textarea", "select", "button"};
    VALID_INPUT_TAGS = Arrays.asList(a);

    try {
      CLASSIFIER = new InputClassifier();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private final Form form;
  private final FIELD_TYPES INPUT_TYPE;
  protected String placeholder = "";
  private String inputTitle;
  private String inputId;
  private Input.CATEGORIES category;
  private final String inputName;
  protected final WebElement webElement;

  ;

  public Input(Form f, WebElement ip, FIELD_TYPES field_type) throws IOException {
    INPUT_TYPE = field_type;
    form = f;
    webElement = ip;
    inputName = getAttr(webElement, "name");
    inputId = getAttr(webElement, "id");
    System.out.printf("PlaceHolder %s%n", placeholder);
    System.out.printf("Name %s%n", inputName);
  }

  protected Form getAssociatedForm() {
    return form;
  }

  public FIELD_TYPES getType() {
    return INPUT_TYPE;
  }

  public WebElement getWebElement() {
    return webElement;
  }

  private Input.CATEGORIES getCategory() {
    return category;
  }

  public String getInputName() {
    return inputName;
  }
  String getTooltipData() {

    return String.format("Input label - %s<br/>Input category - %s<br/>Input orientation - ", "", getCategory().toString());
  }

  public abstract void fill(String s);

  private Input.CATEGORIES findCategory(String text) {
    if (getType().equals(FIELD_TYPES.BUTTON_INPUT)) {
      return Input.CATEGORIES.ACTION_ITEM;
    } else {
      return Input.CLASSIFIER.getCategory(text);
    }
  }

  @Override
  public String toString() {
    return String.format("Name:  %s | Id:   %s |   LabelText:  %s%n", this.getInputName(), this.getInputId(), this.getInputTitle());
  }
  void setInputTitle(String input_title) {
    this.inputTitle = input_title;
  }
  String getInputTitle() {
    return inputTitle;
  }
  String getInputId() {
    return inputId;
  }
  public void setInputId(String inputId) {
    this.inputId = inputId;
  }
  void printFieldLabelAssociation() {
    System.out.println(this.toString());
  }

  public static void setLabel(Input input, String labelText) {
    input.setInputTitle(labelText);
    input.printFieldLabelAssociation();
  }
  public static enum FIELD_TYPES {
    TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT, FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT
  }

  public static enum CATEGORIES {
    EMAIL, JOB_SEARCH, NAME, AGE, DOB, GENDER, DESC, PASSWORD, STATE, CITY,
    NATIONALITY, COUNTRY, CONTACT, ADDRESS, PIN_CODE, USERNAME, T_AND_C,
    ACTION_ITEM, UNDEFINED
  }
}
