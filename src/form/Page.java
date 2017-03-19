package form;

import form.ml.LRClassifier;
import form.util.SeleniumUtil;
import formcrawl.FormCrawl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Page {

  private static final String SEARCHABLE_FORM_LABEL = "s";
  private static final String CHROME_DRIVER_PATH = "/Users/ptilak/bin/chromedriver";
  private static final String NOT_SEARCHABLE_MSG = "Form is not a searchable";
  private final URL pageUrl;
  private final ArrayList<Form> forms;
  private WebDriver driver;

  public Page(String pageUrl) throws Exception {
    SeleniumUtil.resetCache();
    System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    this.pageUrl = new URL(pageUrl);
    this.forms = new ArrayList<>();
    this.loadSeleniumDriver();
    System.out.println("Selenium driver loaded");
    this.findForms();
    System.out.println("All forms parsed");
    FormCrawl.drivers.add(this.driver);

    for (Form f : this.forms) {
      f.submitForm();
    }
  }

  private void loadSeleniumDriver() {
    WebDriver driver = new ChromeDriver();
    this.driver = driver;
    driver.get(this.pageUrl.toString());
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  private void findForms() {

    By formTag = By.tagName("form");
    List<WebElement> forms = this.driver.findElements(formTag);

    for (WebElement form : forms) {
      //checkIfSearchableForm(form);
      Form f = new Form(this, form);
      this.forms.add(f);
    }
  }
  private void checkIfSearchableForm(WebElement form) {
    try {
      String formText = Form.getTextForClassification(form, this);
      LRClassifier classifier = new LRClassifier();
      String classifiedFormLabel = classifier.classifyLabel(formText);
      if (!classifiedFormLabel.equals(SEARCHABLE_FORM_LABEL)) {
        JOptionPane.showMessageDialog(null, NOT_SEARCHABLE_MSG);
        System.exit(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public WebDriver getDriver() {
    return this.driver;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
