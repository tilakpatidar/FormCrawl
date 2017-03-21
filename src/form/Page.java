package form;

import form.ml.LRClassifier;
import form.util.SeleniumUtil;
import formcrawl.FormCrawl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public final class Page {

  private static final String SEARCHABLE_FORM_LABEL = "s";
  private static final String CHROME_DRIVER_PATH = "/Users/ptilak/bin/chromedriver";
  private static final String NOT_SEARCHABLE_MSG = "Form is not a searchable";
  private final URL pageUrl;
  private final ArrayList<Form> forms;
  private WebDriver driver;

  static {
    System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    SeleniumUtil.resetCache();
  }

  public Page(String pageUrl) throws Exception {
    this.pageUrl = new URL(pageUrl);
    this.forms = new ArrayList<>();
    this.loadSeleniumDriver();
    this.findForms();
    FormCrawl.drivers.add(this.driver);
    promptForNextCrawlIteration();
  }
  private void promptForNextCrawlIteration() throws Exception {
    Scanner sc = new Scanner(System.in);
    String line;
    System.out.println("Next iteration (y/n) ?");
    while ((line = sc.nextLine()) != null && line.equals("y")) {
      System.out.println("Starting . . .");
      Form f = this.forms.get(0);
      f.submitForm();
      System.out.println("Next iteration (y/n) ?");
    }
    System.exit(0);
  }

  private void loadSeleniumDriver() {
    driver = new ChromeDriver();
    driver.get(this.pageUrl.toString());
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    System.out.println("Selenium driver loaded");
  }

  private void findForms() {

    By formTag = By.tagName("form");
    List<WebElement> forms = this.driver.findElements(formTag);
    WebElement form = forms.get(0);
    quitAppIfNotSearchable(form);
    Form f = null;
    try {
      f = new Form(this, form);
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
    this.forms.add(f);
    System.out.println("All forms parsed");
  }
  private void quitAppIfNotSearchable(WebElement form) {
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

  WebDriver getDriver() {
    return this.driver;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
