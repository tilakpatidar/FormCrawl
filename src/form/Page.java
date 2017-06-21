package form;

import form.ml.LRClassifier;
import form.util.SeleniumUtil;
import form.util.WElement;
import formcrawl.FormCrawl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public final class Page {

  private static final String SEARCHABLE_FORM_LABEL = "s";
  private static final String CHROME_DRIVER_PATH = "/Users/ptilak/bin/chromedriver";
  private static final String NOT_SEARCHABLE_MSG = "Form is not a searchable";
  private final URL pageUrl;
  private static LRClassifier classifier = LRClassifier.load();

  static {
    System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    SeleniumUtil.resetCache();
  }

  private final Form form;
  private final String source;
  private final Document soup;

  public Page(String pageUrl) throws Exception {
    this.pageUrl = new URL(pageUrl);
    this.loadSeleniumDriver();
    String pageSource = FormCrawl.driver.getPageSource();
    this.soup = Jsoup.parse(pageSource);
    this.source = pageSource;
    this.form = findForm();
    promptForNextCrawlIteration();
  }
  private void promptForNextCrawlIteration() throws Exception {
    Scanner sc = new Scanner(System.in);
    String line;
    System.out.println("Next iteration (y/n) ?");
    while ((line = sc.nextLine()) != null && line.equals("y")) {
      System.out.println("Starting . . .");
      this.form.submitForm();
      System.out.println("Next iteration (y/n) ?");
    }
    System.exit(0);
  }

  private void loadSeleniumDriver() {
    FormCrawl.driver = new ChromeDriver();
    FormCrawl.driver.get(this.pageUrl.toString());
    FormCrawl.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    System.out.println("Selenium driver loaded");
  }

  private Form findForm() {

    By formTag = By.tagName("form");
    WElement fdom = new WElement(FormCrawl.driver.findElement(formTag));
    Document formSoup = Jsoup.parse(fdom.toString());
    quitAppIfNotSearchable(formSoup);
    Form f = null;
    try {
      f = new Form(this, fdom, formSoup);
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }

    System.out.println("All forms parsed");
    return f;
  }
  private void quitAppIfNotSearchable(Document formSoup) {
    try {
      String formText = Form.getTextForClassification(formSoup);
      String classifiedFormLabel = classifier.classifyLabel(formText);
      if (!classifiedFormLabel.equals(SEARCHABLE_FORM_LABEL)) {
        JOptionPane.showMessageDialog(null, NOT_SEARCHABLE_MSG);
        System.exit(0);
      }
      System.out.println("Form detected is a searchable form");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
  public String getSource() {
    return source;
  }
}
