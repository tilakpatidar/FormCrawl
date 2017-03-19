package form;

import form.autofill.data.Record;
import form.autofill.fillers.AutoFill;
import form.autofill.fillers.RandomAutoFill;
import form.autofill.suggesters.RandomSuggester;
import form.autofill.suggesters.Suggester;
import form.inputs.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static form.Input.setLabel;
import static form.util.SeleniumUtil.*;
import static form.util.TextUtil.*;

public class Form {

  public static final By RESULTS_DIV = By.id("inner_results_div");
  private static final Class<RandomAutoFill> FILL_CLASS = RandomAutoFill.class;
  private static final Class<RandomSuggester> SUGGESTER_CLASS = RandomSuggester.class;
  private final Page page;

  private final WebElement formDom;
  private final Suggester suggester;
  private final WebDriver driver;
  private ArrayList<Input> form_inputs;
  private HashMap<String, Group> inputGroups = new HashMap<>();
  private HashMap<Input, WebElement> inputToWebElement = new HashMap<>();
  private HashMap<WebElement, Input> webElementToInput = new HashMap<>();
  private Button submitButton;

  public Form(Page page, WebElement formDom) throws IllegalAccessException, InstantiationException {

    this.page = page;
    this.formDom = formDom;
    this.suggester = SUGGESTER_CLASS.newInstance();
    this.driver = this.getAssociatedPage().getDriver();
    ArrayList<WebElement> input_collection = Form.detectFields(this.formDom);
    System.out.println("Web Elements detected from form DOM");
    this.form_inputs = new ArrayList<>();

    for (WebElement input : input_collection) {
      try {
        Input input_obj = this.detectInput(input);
        inputToWebElement.put(input_obj, input_obj.getWebElement());
        webElementToInput.put(input_obj.getWebElement(), input_obj);
        this.form_inputs.add(input_obj);
        if (input_obj instanceof Button) {
          Button b = (Button) input_obj;
          if (b.getButtonType().equals(Button.TYPES.SUBMIT)) {
            this.submitButton = b;
            System.out.println("Submit button detected");
          }
        }
        // this.page.createTooltip(input_obj.getWebElement(), input_obj.getTooltipData());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    associateLabelsAndFields();

    if (this.submitButton == null) {
      throw new RuntimeException("No submit button present");
    }
  }
  public static int angleBetween2Lines(Point labelLocation, Point
      fieldLocation) {
    Point basePoint = new Point(fieldLocation.x + 10, fieldLocation.y);
    float angle1 = (float) Math.atan2(fieldLocation.y - labelLocation.y, labelLocation.x
        - fieldLocation.x);
    float angle2 = (float) Math.atan2(basePoint.y - fieldLocation.y, fieldLocation.x
        - basePoint.x);
    float calculatedAngle = (float) Math.toDegrees(angle1 - angle2);
    if (calculatedAngle < 0) calculatedAngle += 360;
    return (int) calculatedAngle;
  }
  public static boolean angleNear90Multiples(int angle) {
    int[] multiples = {0, 90, 180, 270, 360};
    double comparableWithinPercentage = 7.0 / 100.0;
    for (int multiple : multiples) {
      if (comparableNum(angle, multiple, comparableWithinPercentage)) {
        return true;
      }
    }
    return false;
  }
  private static boolean comparablePoint(Point labelPoint, Point fieldPoint) {
    double comparableWithinPercentage = 7.0 / 100.0;

    double x = labelPoint.x;
    double x1 = fieldPoint.x;
    double y = labelPoint.y;
    double y1 = fieldPoint.y;
    boolean comparableX = comparableNum(x, x1, comparableWithinPercentage);
    boolean comparableY = comparableNum(y, y1, comparableWithinPercentage);
    return comparableX || comparableY;
  }
  private static boolean comparableNum(double x, double y, double
      withinPercentage) {
    double xComparision = Math.abs(1.0 - (x / y));
    return xComparision <= withinPercentage;
  }
  private static ArrayList<WebElement> detectFields(WebElement form_dom) {

    ArrayList<WebElement> input_collection = new ArrayList<>();
    String[] VALID_INPUT_TAGS = {"input", "textarea", "select", "button"};
    for (String tag : VALID_INPUT_TAGS) {
      input_collection.addAll(form_dom.findElements(By.tagName(tag)));
    }

    ArrayList<WebElement> form_elements = new ArrayList<>();

    for (WebElement e : input_collection) {
      //not hidden element and element is part of valid input tags
      String tagName = e.getTagName();
      int index = Arrays.binarySearch(VALID_INPUT_TAGS, tagName);
      boolean hasValidTagName = index >= 0;
      if (!isHiddenInputElement(e) && hasValidTagName) {
        form_elements.add(e);
      }
    }
    return form_elements;
  }
  static String getTextForClassification(WebElement formElement, Page page) {
    String pageTitle = page.getDriver().getTitle();
    String formText = formElement.getText();
    By submitButton = By.cssSelector("input[type='submit']");
    By submitButton2 = By.cssSelector("button[type='submit']");
    List<String> placeHolders = getElementByAttr(formElement, "placeholder");
    List<String> names = getElementByAttr(formElement, "name");
    String btn_text;
    String btn_text2;
    try {
      btn_text = formElement.findElement(submitButton).getAttribute("value");
    } catch (NoSuchElementException e) {
      btn_text = "";
    }

    try {
      btn_text2 = formElement.findElement(submitButton2).getText();
    } catch (NoSuchElementException e) {
      btn_text2 = "";
    }

    List<String> labelText = getTextOf(formElement, By.cssSelector("label"));

    String text = String.format("%s %s %s %s %s %s %s", pageTitle, formText, concatList(placeHolders, " "), concatList(names, " "), btn_text, btn_text2, concatList(labelText, " "));

    String camelCaseStr = removePunctuations(filterText(text)).toLowerCase();

    ArrayList<String> tokens = splitCamelCaseString(camelCaseStr);
    String tokenStr = tokens.stream().reduce((a, b) -> a + " " + b).orElse("");
    return removeMultipleSpaces(tokenStr);
  }
  private void associateLabelsAndFields() {
    HashMap<WebElement, Input> cloned = new HashMap<>(webElementToInput);
    Set<Map.Entry<WebElement, Input>> pairs = cloned.entrySet();
    List<WebElement> labelsWithoutFor = filterLabelWhichHaveForAttr(pairs);
    for (Map.Entry<WebElement, Input> pair : pairs) {
      WebElement field = pair.getKey();
      Input input = pair.getValue();
      if (fieldIsAButton(field, input)) {
        continue;
      }
      WebElement goodLabel = null;
      int minDist = 99999;
      boolean foundEarlierMatch = false;

      for (WebElement label : labelsWithoutFor) {
        Point labelPoint = getPointFor(label);
        Point fieldPoint = getPointFor(field);
        String labelText = getLabelTextFor(label);
        String fieldId = getAttr(field, "id");

        //check if same parent and only text
        WebElement labelParent = getParent(label);
        WebElement fieldParent = getParent(field);
        if (labelParent.equals(fieldParent)) {
          if (filterText(getLabelTextFor(labelParent)).equals(filterText(labelText))) {
            setLabel(input, labelText);
            labelsWithoutFor.remove(label);
            foundEarlierMatch = true;
            break;
          }
        }

        if (comparablePoint(labelPoint, fieldPoint)) {
          int distance = calculateDist(labelPoint, fieldPoint);
          int angle = angleBetween2Lines(labelPoint, fieldPoint);
          if (angleNear90Multiples(angle) && distance < minDist) {
            goodLabel = label;
            minDist = distance;
//            System.out.printf("accepted %d  %s  |  %s%n", angle, fieldId, labelText);
          } else {
//            System.out.printf("rejected %d  %s  |  %s%n", angle, fieldId, labelText);
          }
        } else {
//          System.out.printf("rejected dist   %s  |  %s%n", fieldId, labelText);
        }
      }

      if (!foundEarlierMatch && goodLabel != null) {
        String labelText = getLabelTextFor(goodLabel);
        setLabel(input, labelText);
        labelsWithoutFor.remove(goodLabel);
      }
    }
  }
  private List<WebElement> filterLabelWhichHaveForAttr(Set<Map
      .Entry<WebElement, Input>> pairs) {
    WebElement formElement = this.getElement();
    List<WebElement> labels = getElementsByTagName(formElement, "label");
    return labels.stream().filter(label -> {
      //search if dev used a label[for=]
      String labelText = getLabelTextFor(label);
      String fieldId = getAttr(label, "for");
      if (fieldId != null) {
        WebElement fieldElement = this.getElement().findElement(By.id(fieldId));
        Input input = webElementToInput.get(fieldElement);
        setLabel(input, labelText);
        Map.Entry<WebElement, Input> entry =
            new AbstractMap.SimpleEntry<>(fieldElement, input);
        pairs.remove(entry);
        return false;
      }
      return true;
    }).collect(Collectors.toList());
  }
  private boolean fieldIsAButton(WebElement field, Input input) {
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
  private int calculateDist(Point first, Point second) {
    int xDiff = second.x - first.x;
    int yDiff = second.y - first.y;
    int xDiffSq = (int) Math.pow(xDiff, 2);
    int yDiffSq = (int) Math.pow(yDiff, 2);
    int sum = xDiffSq + yDiffSq;
    return (int) Math.sqrt(sum);
  }
  public ArrayList<Input> getAssociatedInputs() {
    return this.form_inputs;
  }
  void submitForm() throws Exception {
    this.fillForm();
    Button b = this.getSubmitButton();
    b.getWebElement().click();
    while (true) {
      try {
        driver.findElement(RESULTS_DIV);
        break;
      } catch (NoSuchElementException e) {
        e.printStackTrace();
      }
      Thread.sleep(5000);
    }
  }
  private Button getSubmitButton() {
    return this.submitButton;
  }
  private void fillForm() throws Exception {
    AutoFill filler = FILL_CLASS.newInstance();
    filler.init(this);
    Record record = suggester.getSuggestedRecord(this);
    filler.fill(record);
  }
  public Input getInputBy(WebElement element) {
    return this.webElementToInput.get(element);
  }
  public Group getGroupBy(String name) {
    return this.inputGroups.get(name);
  }
  private Input detectInput(WebElement ip) throws IOException {
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
  public Page getAssociatedPage() {
    return this.page;
  }
  WebElement getElement() {
    return this.formDom;
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
    return this.form_inputs.stream().filter(i -> i.isBounded() && !i.isGroupAble()).collect(Collectors.toList());
  }
  public List<Input> getUnboundedFields() {
    return this.form_inputs.stream().filter(i -> !i.isBounded()).collect(Collectors.toList());
  }
}

