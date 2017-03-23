package form;

import form.autofill.data.Record;
import form.autofill.fillers.AutoFill;
import form.autofill.fillers.RandomAutoFill;
import form.autofill.suggesters.RandomSuggester;
import form.autofill.suggesters.Suggester;
import form.inputs.*;
import form.util.DomCompare;
import form.util.WElement;
import formcrawl.FormCrawl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static form.Input.setLabel;
import static form.util.PointUtil.*;
import static form.util.SeleniumUtil.*;
import static form.util.TextUtil.*;

public class Form {

  private static final Class<RandomAutoFill> FILL_CLASS = RandomAutoFill.class;
  private static final Class<RandomSuggester> SUGGESTER_CLASS = RandomSuggester.class;
  private static final String[] LABEL_TAGS = {"label", "span", "td", "div"};
  private static final int WAIT_FOR_RESULTS = 10000;

  private final WElement formDom;
  private final Suggester suggester;
  private ArrayList<Input> formInputs;
  private HashMap<String, Group> inputGroups = new HashMap<>();
  private HashMap<WElement, Input> webElementToInput = new HashMap<>();
  private Button submitButton;
  private final String cssSelector;
  private final DomCompare domCompare;
  private Element previousResults;
  private final Document soup;
  private final List<WElement> labelsElements = new ArrayList<>();
  private final List<WElement> labelsWithoutFor = new ArrayList<>();

  private final List<WElement> associatedFields = new ArrayList<>();

  public Form(Page page, WElement formDom, Document soup) throws
      IllegalAccessException,
      InstantiationException {

    this.formDom = formDom;
    this.soup = soup;
    this.suggester = SUGGESTER_CLASS.newInstance();
    this.domCompare = new DomCompare(page.getSource());
    this.cssSelector = this.getCSSSelector();
    this.formInputs = new ArrayList<>();

    for (WElement inputDom : Form.detectFields(this.formDom)) {
      try {
        Input input_obj = this.detectInput(inputDom);
        webElementToInput.put(inputDom, input_obj);
        this.formInputs.add(input_obj);
        if (input_obj instanceof Button) {
          Button b = (Button) input_obj;
          if (b.getButtonType().equals(Button.TYPES.SUBMIT)) {
            this.submitButton = b;
            System.out.println("Submit button detected");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    associateLabelsAndFields();

    if (this.submitButton == null) {
      throw new RuntimeException("No submit button present");
    }
  }

  private static ArrayList<WElement> detectFields(WElement form_dom) {

    ArrayList<WElement> input_collection = new ArrayList<>();
    String[] VALID_INPUT_TAGS = {"input", "textarea", "select", "button"};
    for (String tag : VALID_INPUT_TAGS) {
      List<WElement> elements = form_dom.findElements(By.tagName(tag));
      elements = elements.stream().filter(e -> !isHiddenInputElement(e)).collect(Collectors.toList());
      input_collection.addAll(elements);
    }

    return input_collection;
  }
  static String getTextForClassification(Document formElement) {
    String pageTitle = FormCrawl.driver.getTitle();
    String formText = formElement.text();
    String submitButton = "input[type='submit']";
    String submitButton2 = "button[type='submit']";
    String placeHolders = formElement.getElementsByAttribute("placeholder").text();
    String names = formElement.getElementsByAttribute("name").text();
    String btn_text;
    String btn_text2;
    try {
      btn_text = formElement.select(submitButton).attr("value");
    } catch (NoSuchElementException e) {
      btn_text = "";
    }

    try {
      btn_text2 = formElement.select(submitButton2).text();
    } catch (NoSuchElementException e) {
      btn_text2 = "";
    }

    String labelText = formElement.getElementsByTag("label").text();

    String text = String.format("%s %s %s %s %s %s %s", pageTitle, formText, placeHolders, names, btn_text, btn_text2, labelText);

    String camelCaseStr = removePunctuations(filterText(text)).toLowerCase();

    ArrayList<String> tokens = splitCamelCaseString(camelCaseStr);
    String tokenStr = tokens.stream().reduce((a, b) -> a + " " + b).orElse("");
    return removeMultipleSpaces(tokenStr);
  }
  private void associateLabelsAndFields() {
    HashMap<WElement, Input> cloned = buildRequiredLabelLists();

    for (Map.Entry<WElement, Input> pair : cloned.entrySet()) {
      WElement field = pair.getKey();
      Input input = pair.getValue();
      if (fieldIsAButton(field, input)) {
        continue;
      }
      WElement goodLabel = tryToAssociateFieldWithAnyOf(field, labelsWithoutFor);
      String labelText = goodLabel == null ? "" : getLabelTextFor(goodLabel);
      setLabel(input, labelText);
      labelsWithoutFor.remove(goodLabel);
    }
  }
  private HashMap<WElement, Input> buildRequiredLabelLists() {
    HashMap<WElement, Input> cloned = new HashMap<>(webElementToInput);
    //search for label tags
    for (String tagName : LABEL_TAGS) {
      labelsElements.addAll(getElementsByTagName(this.formDom, tagName));
    }

    //filter labels without for
    //filter labels with for
    labelsElements.forEach(label -> {
      String fieldId = getAttr(label, "for");
      if (fieldId == null) {
        labelsWithoutFor.add(label);
      } else {
        WElement element = this.formDom.findElement(By.id(fieldId));
        Input inputObj = this.webElementToInput.get(element);
        setLabel(inputObj, getLabelTextFor(label));
        associatedFields.add(element);
      }
    });
    associatedFields.forEach(cloned::remove);
    return cloned;
  }
  private WElement tryToAssociateFieldWithAnyOf(WElement field, List<WElement> labelsWithoutFor) {
    WElement goodLabel = null;
    int minDist = 99999;

    for (WElement label : labelsWithoutFor) {
      Point labelPoint = getPointFor(label);
      Point fieldPoint = getPointFor(field);
      String labelText = getLabelTextFor(label);
      if (labelText.trim().isEmpty()) {
        continue;
      }
      if (ifLabelTextSameAsParent(field, label, labelText)) {
        return label;
      }

      if (comparablePoint(labelPoint, fieldPoint)) {
        int distance = calculateDist(labelPoint, fieldPoint);
        int angle = angleBetween2Lines(labelPoint, fieldPoint);
        if (angleNear90Multiples(angle) && distance < minDist) {
          goodLabel = label;
          minDist = distance;
        }
      }
    }

    return goodLabel;
  }
  private boolean ifLabelTextSameAsParent(WElement field, WElement label, String labelText) {
    WElement labelParent = getParent(label);
    WElement fieldParent = getParent(field);
    if (labelParent.equals(fieldParent)) {
      if (filterText(getLabelTextFor(labelParent)).equals(filterText(labelText))) {
        return true;
      }
    }
    return false;
  }

  private boolean fieldIsAButton(WElement field, Input input) {
    if (isFieldButton(field)) {
      setLabel(input, getLabelTextFor(field));
      return true;
    }
    if (isFieldInputButton(field)) {
      setLabel(input, getAttr(field, "value"));
      return true;
    }
    return false;
  }

  void submitForm() throws Exception {
    this.resetForm();
    this.fillForm();
    Button b = this.getSubmitButton();
    b.getWElement().click();
    System.out.println("Form submitted waiting for results ...");
    Thread.sleep(WAIT_FOR_RESULTS);
    this.previousResults = this.domCompare.getResultsDoc(FormCrawl.driver.getPageSource());
  }
  public Element getPreviousResults() {
    return this.previousResults;
  }
  private void resetForm() {
    String jsScript = "var form = document.querySelectorAll(\"" + this.cssSelector + "\")[0]; form.reset();";
    JavascriptExecutor executor = (JavascriptExecutor) FormCrawl.driver;
    executor.executeScript(jsScript);
  }

  private String getCSSSelector() {
    String id = getAttr(formDom, "id");
    if (isValidAttr(id)) {
      return "#" + id;
    }
    String className = getAttr(formDom, "class");
    if (isValidAttr(className)) {
      return "form." + className;
    }
    String action = getAttr(formDom, "action");
    if (isValidAttr(action)) {
      return "form[action='" + action + "']";
    }

    return "form";
  }

  private Button getSubmitButton() {
    return this.submitButton;
  }
  private void fillForm() throws Exception {
    AutoFill filler = FILL_CLASS.newInstance();
    filler.init(this);
    Record record = suggester.getSuggestedRecord(this);
    filler.fill(record);
    System.out.println("Form filled");
  }
  public Group getGroupBy(String name) {
    return this.inputGroups.get(name);
  }
  private Input detectInput(WElement ip) throws IOException {
    String tag_name = ip.getTagName();
    Input inp;
    Button bt;
    switch (tag_name) {
      case "input":
        String input_type = ip.getAttribute("type").toLowerCase();
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
            inp = new Text(this, ip);
            break;
          //default input type in html is text box
        }
        break;

      case "select":
        inp = new Select(this, ip);
        break;
      case "button":
        String in = ip.getAttribute("type").toLowerCase();
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
        inp = new Text(this, ip);
        //default input in HTML is text box
    }

    return inp;
  }
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
  public void addToGroup(Group gp) {
    this.inputGroups.put(gp.getName(), gp);
  }
  public List<Group> getInputGroups() {
    return new ArrayList<>(this.inputGroups.values());
  }
  public List<Input> getNonGroupableBoundedInputs() {
    return this.formInputs.stream().filter(i -> i.isBounded() && !i.isGroupAble()).collect(Collectors.toList());
  }
  public List<Input> getUnboundedFields() {
    return this.formInputs.stream().filter(i -> !i.isBounded()).collect(Collectors.toList());
  }
}

