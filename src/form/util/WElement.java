package form.util;

import formcrawl.FormCrawl;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WElement{
  private String cssSelector;
  private WebElement element;
  private static String jsCode;
  static {
    try {
      File source = new File("./js_scripts/css_path.js");
      jsCode = new Scanner(source).useDelimiter("\\Z").next();
      System.out.println(jsCode);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  public WElement(WebElement element){
    this.cssSelector = evaluateSelector(element);
    this.element = element;
  }
  private String evaluateSelector(WebElement element) {
    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) FormCrawl.driver;
    javascriptExecutor.executeScript(jsCode + "alert(UTILS.cssPath" +
        "(arguments[0]));" +
        "", element);
    return "";
  }
  public String getCssSelector() {
    return cssSelector;
  }
  public WebElement ge() {
    return element;
  }
}
