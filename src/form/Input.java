package form;

import org.openqa.selenium.WebElement;

import java.io.IOException;

import static form.util.SeleniumUtil.getAttr;

public abstract class Input {

  private final Form form;
  private final FIELD_TYPES INPUT_TYPE;
  protected String placeholder = "";
  private String inputTitle;
  private String inputId;
  private final String inputName;
  protected final WebElement webElement;

  public Input(Form f, WebElement ip, FIELD_TYPES field_type) throws IOException {
    INPUT_TYPE = field_type;
    form = f;
    webElement = ip;
    inputName = getAttr(webElement, "name");
    inputId = getAttr(webElement, "id");
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

  protected String getInputName() {
    return inputName;
  }

  @Override
  public String toString() {
    return String.format("Name:  %s | Id:   %s |   LabelText:  %s%n", this.getInputName(), this.getInputId(), this.getInputTitle());
  }

  private void setInputTitle(String input_title) {
    this.inputTitle = input_title;
  }
  private String getInputTitle() {
    return inputTitle;
  }
  private String getInputId() {
    return inputId;
  }
  private void printFieldLabelAssociation() {
    System.out.println(this.toString());
  }
  static void setLabel(Input input, String labelText) {
    input.setInputTitle(labelText);
    input.printFieldLabelAssociation();
  }

  public enum FIELD_TYPES {
    TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT, BUTTON_INPUT, EMAIL_INPUT, PASSWORD_INPUT, HIDDEN_INPUT;
  }

  public abstract boolean isGroupAble();

  public abstract boolean isBounded();

  public abstract void fill(String s);
}
