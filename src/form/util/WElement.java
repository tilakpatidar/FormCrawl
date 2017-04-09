package form.util;

import formcrawl.FormCrawl;
import org.openqa.selenium.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WElement {
  private String cssSelector;
  private WebElement element;
  private static String jsCode;

  static {
    try {
      File source = new File("./js_scripts/css_path.js");
      jsCode = new Scanner(source).useDelimiter("\\Z").next();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public WElement(WebElement element) {
    this.cssSelector = evaluateSelector(element);
    this.element = element;
  }
  private String evaluateSelector(WebElement element) {
    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) FormCrawl.driver;
    Object o = javascriptExecutor.executeScript(jsCode + "return UTILS" +
        ".cssPath" +
        "(arguments[0]);" +
        "", element);
    return (String) o;
  }
  public WebElement ge() {
    try {
      // Calling any method forces a staleness check
      element.isEnabled();
    } catch (StaleElementReferenceException expected) {
//      System.out.println("refresh");
      this.element = FormCrawl.driver.findElement(By.cssSelector(cssSelector));
    }
    return element;
  }
  public List<WElement> findElements(By selector) {
    return this.ge().findElements(selector).stream().map(e -> new WElement(e)
    ).collect(Collectors.toList());
  }
  public String getText() {
    return this.ge().getText();
  }
  public String getAttribute(String attr) {
    return this.ge().getAttribute(attr);
  }
  public String getTagName() {
    return this.ge().getTagName();
  }
  public Point getLocation() {
    return this.ge().getLocation();
  }
  public WElement findElement(By xpath) {
    try{
      return new WElement(this.ge().findElement(xpath));
    }catch (NoSuchElementException e){
      return null;
    }
  }
  public void click() {
    this.ge().click();
  }
  public void clear() {
    this.ge().clear();
  }
  public void sendKeys(String value) {
    this.ge().sendKeys(value);
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WElement wElement = (WElement) o;

    if (cssSelector != null ? !cssSelector.equals(wElement.cssSelector) : wElement.cssSelector != null)
      return false;
    return element != null ? element.equals(wElement.element) : wElement.element == null;
  }
  @Override
  public int hashCode() {
    int result = cssSelector != null ? cssSelector.hashCode() : 0;
    result = 31 * result + (element != null ? element.hashCode() : 0);
    return result;
  }
  public String getCssSelector() {
    return cssSelector;
  }
}
