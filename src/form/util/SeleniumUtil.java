package form.util;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class SeleniumUtil {
  private static HashMap<WebElement, Point> pointStore = new HashMap<>();
  private static HashMap<WebElement, String> labelStore = new HashMap<>();
  private static HashMap<WebElement, WebElement> parentStore = new HashMap<>();
  private static HashMap<String, HashMap<WebElement, String>> attrStore = new
      HashMap<>();
  public static List<String> getTextOf(WebElement formElement, By selector) {
    return formElement.findElements(selector).stream().map((e) -> e.getText()).collect(Collectors.toList());
  }

  public static List<String> getElementByAttr(WebElement formElement, String cssSelector) {
    By selector = By.cssSelector("[ " + cssSelector + " ]");
    return formElement.findElements(selector).stream().map((e) -> e.getAttribute(cssSelector)).collect(Collectors.toList());
  }

  public static boolean isFieldButton(WebElement field) {
    if (field.getTagName().equalsIgnoreCase("button")) {
      return true;
    }
    return false;
  }
  public static boolean isFieldInputButton(WebElement field) {
    if (field.getTagName().equalsIgnoreCase("input")) {
      boolean isSubmit = field.getAttribute("type").equalsIgnoreCase("submit");
      boolean isReset = field.getAttribute("type").equalsIgnoreCase("reset");
      return isSubmit || isReset;
    }
    return false;
  }

  public static List<WebElement> getElementsByTagName(WebElement element, String tag) {
    return element.findElements(By.tagName(tag));
  }

  public static String getAttr(WebElement element, String attr) {
//    attr = attr.toLowerCase();
//    HashMap<WebElement, String> fetchedAttr = attrStore.get(attr);
//    System.out.println(fetchedAttr);
//    if (fetchedAttr != null) {
//      String value = fetchedAttr.get(element);
//      if (value != null) {
//        return value;
//      }
//      String latestAttr = element.getAttribute(attr);
//      fetchedAttr.put(element, latestAttr);
//      return latestAttr;
//    }
//    String latestAttr = element.getAttribute(attr);
//    System.out.println(latestAttr);
//    HashMap<WebElement, String> attrPair = new HashMap<>();
//    attrPair.put(element, latestAttr);
//    attrStore.put(attr, attrPair);
//    return latestAttr;
    return element.getAttribute(attr);
  }

  public static boolean hasAttr(WebElement e, String attr) {
    String fetchedAttr = getAttr(e, attr);
    return isValidAttr(fetchedAttr);
  }
  public static boolean isValidAttr(String fetchedAttr) {
    return fetchedAttr != null && !fetchedAttr.trim().isEmpty();
  }

  private static boolean attrEq(WebElement element, String attr, String value) {
    String attribute = getAttr(element, attr);
    return attribute != null && attribute.equalsIgnoreCase(value);
  }

  public static boolean isHiddenInputElement(WebElement e) {
    return attrEq(e, "type", "hidden");
  }

  public static Point getPointFor(WebElement element) {
    Point point = pointStore.get(element);
    if (point != null) {
      return point;
    }
    Point location = element.getLocation();
    pointStore.put(element, location);
    return location;
  }

  public static String getLabelTextFor(WebElement label) {
    String labelStr = labelStore.get(label);
    if (labelStr != null) {
      return labelStr;
    }
    String labelS = label.getAttribute("innerText");
    labelStore.put(label, labelS);
    return labelS;
  }

  public static WebElement getParent(WebElement label) {
    WebElement element = parentStore.get(label);
    if (element != null) {
      return element;
    }
    WebElement parent = label.findElement(By.xpath(".."));
    parentStore.put(label, parent);
    return parent;
  }
  public static void resetCache() {
    parentStore.clear();
    labelStore.clear();
    pointStore.clear();
    attrStore.clear();
  }
}
