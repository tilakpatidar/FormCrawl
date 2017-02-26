/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import formcrawl.FormCrawl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tilak
 */
public final class Page {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final String CHROME_DRIVER_PATH = "/Users/ptilak/bin/chromedriver";
  final URL pageUrl;
  /**
   * List of all the forms in the page
   */
  private final ArrayList<Form> forms;
  private String pageSource;
  private Document soup;
  private WebDriver driver;

  /**
   * Constructor to initiate form processing
   *
   * @param pageUrl - The string representation web page's url
   * @throws MalformedURLException  - When page url is not valid
   * @throws FileNotFoundException  - When js scripts not found
   * @throws InvalidObjectException - When driver is not instance of
   *                                JavascriptExecutor
   */
  public Page(String pageUrl) throws Exception {
    System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    this.pageUrl = new URL(pageUrl);
    this.forms = new ArrayList<>();

    this.processSeleniumChoice();
    this.removeCSS();
    this.createDOM();
    this.findForms();

    //restoring old dom once page object is created
    if (this.driver instanceof JavascriptExecutor) {
      ((JavascriptExecutor) driver)
          .executeScript("window.restoreOldDom();");
    }

    FormCrawl.drivers.add(this.driver);

    for (Form f : this.forms) {
      f.submitForm();
    }
  }

  private void processSeleniumChoice() {
    LOGGER.log(Level.INFO, "[INFO] Starting chrome driver");
    WebDriver driver = new ChromeDriver();
    this.driver = driver;
    driver.get(this.pageUrl.toString());
    LOGGER.log(Level.INFO, "[INFO] Selenium waiting for js to load");
    //wait for 10 seconds before capturing HTML content
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    this.pageSource = driver.getPageSource();
  }

  /**
   * Creates and filter HTML DOM elements.
   */
  private void createDOM() {
    this.pageSource = this.pageSource.trim().replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ");
    this.soup = Jsoup.parse(this.pageSource);
    this.soup.select("script, style, head, .hidden, noscript, img, iframe, header, footer, br, code, nav").remove();
    LOGGER.log(Level.INFO, "[INFO] HTML source filtered");
  }

  private void findForms() {

    Elements foundForms = this.soup.body().getElementsByTag("FORM");
    LOGGER.log(Level.INFO, "[INFO] Found {0} forms in the page", foundForms.size());

    int i = 1;
    for (Element form : foundForms) {
      //create form objects
      try {
        LOGGER.log(Level.INFO, "[INFO] creating form {0}", i);
        Form f = new Form(this, form);
        this.forms.add(f);
      } catch (Exception e) {
        LOGGER.log(Level.INFO, "[ERROR] Error in creating form {0}", i);
        LOGGER.log(Level.FINEST, "[ERROR] Error in creating form " + i, e);
      } finally {
        i++;
      }
    }
  }

  private void removeCSS() throws FileNotFoundException, InvalidObjectException {
    LOGGER.log(Level.INFO, "[INFO] Removing CSS to analyze page");
    try {
      File f = new File("./js_scripts/remove_css.js");
      String code = new Scanner(f).useDelimiter("\\Z").next();
      if (this.driver instanceof JavascriptExecutor) {
        Object executeScript = ((JavascriptExecutor) driver).executeScript(code);
        WebElement element = this.driver.findElement(By.cssSelector("body"));
        this.createTooltip(element, "CSS removed for analyzing page structure");
        return;
      }
      throw new InvalidObjectException("Driver is not instance of Javascript " +
          "executor");
    } catch (IOException e) {
      LOGGER.log(Level.INFO, "[FAIL] Error in reading remove_css.js script");
      LOGGER.log(Level.FINEST, "[ERROR] ", e);
      throw e;
    }
  }

  WebDriver getDriver() {
    return this.driver;
  }

  /**
   * Captures screenshot of element
   *
   * @param e - Takes jsoup element
   * @return - Returns file name of the screenshot file
   */
  private String getElementScreenShot(Element e, int x, int y, int w, int h) throws IOException {
    LOGGER.log(Level.INFO, "[INFO] Starting element screenshot");
    LOGGER.log(Level.FINEST, "[DEBUG] " + x + "" + y + " " + w + " " + h);

    // TODO Think about raster exception
    //avoiding out of dimension error
    if (x < 0) {
      x = 0;
    }

    if (y < 0) {
      y = 0;
    }

    File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    BufferedImage img = ImageIO.read(screen);
    // TODO Is -2 required?
    BufferedImage dest = img.getSubimage(x, y + 2, w, h); //-2 to adjust black borders
    ImageIO.write(dest, "png", screen);
    String random_name = "./screenshots/" + "screen" + e.hashCode() + "" + (int) (Math.random() * 1000) + ".png";
    FileUtils.copyFile(screen, new File(random_name));
    LOGGER.log(Level.FINER, "[DEBUG] " + random_name);
    return random_name;
  }

  String getTopLabelScreenshot(Input inp) throws IOException {
    LOGGER.log(Level.INFO, "[INFO] Starting top label screenshot calculation");
    Element input = inp.getElement();

    WebElement element = this.driver.findElement(By.cssSelector(input.cssSelector()));

    int x, y, w, h;

    Point up = element.getLocation();
    y = element.getLocation().getY() - 40; //font size is 32 px default
    x = up.getX();
    w = element.getSize().getWidth();
    h = 30;
    LOGGER.log(Level.FINEST, "[DEBUG] " + x + "\t" + y + "\t" + w + "\t" + h);
    return this.getElementScreenShot(input, x, y, w, h);
  }

  /**
   * @param inp - Takes an object of type Input
   * @return - Returns string file name of the screenshot
   */
  String getTopLabelFieldScreenshot(Input inp) throws IOException {
    LOGGER.log(Level.INFO, "[INFO] Starting top label field screenshot calculation");
    Element input = inp.getElement();

    WebElement element = this.driver.findElement(By.cssSelector(input.cssSelector()));

    int x, y, w, h;

    Point up = element.getLocation();
    y = element.getLocation().getY() - 35; //font size is 32 px default
    x = up.getX() - 5;
    w = element.getSize().getWidth() + 200;
    h = element.getSize().getHeight() + 200;

    LOGGER.log(Level.FINEST, "[DEBUG] " + x + "" + y + " " + w + " " + h);
    return this.getElementScreenShot(input, x, y, w, h);
  }

  public String getLeftLabelScreenshot(Input inp) throws IOException {
    LOGGER.log(Level.INFO, "[INFO] Starting left label screenshot calculation");
    Element input = inp.getElement();

    WebElement element = this.driver.findElement(By.cssSelector(input.cssSelector()));
    WebElement form_element = this.driver.findElement(By.cssSelector(inp.getAssociatedForm().getElement().cssSelector()));
    int x, y, w, h;

    y = element.getLocation().getY() - 5; //font size is 32 px default
    int diff = element.getLocation().getX() - form_element.getLocation().getX();
    x = form_element.getLocation().getX();
    w = diff;
    h = element.getSize().getHeight() + 10;

    LOGGER.log(Level.FINEST, "[DEBUG] " + x + "" + y + " " + w + " " + h);
    return this.getElementScreenShot(input, x, y, w, h);
  }

  void createTooltip(WebElement element, String text) {
    LOGGER.log(Level.INFO, "[INFO] Creating tooltip");
    Point point = element.getLocation();
    int xcord = point.getX();
    int new_x_loc = xcord + element.getSize().getWidth() + 15;
    int new_y_loc = point.getY() + element.getSize().height;

    if (element.getTagName().equals("body")) {
      new_y_loc = 20;
      new_x_loc = element.getSize().getWidth() / 2 - 50;
    }

    String code = "var new_item = document.createElement('SPAN'); new_item.innerHTML = '" + text + "'; new_item.setAttribute('name','hacked_css_123'); new_item.setAttribute('style', 'position: absolute !important; left:" + new_x_loc + "px !important; top: " + new_y_loc + "px !important; '); new_item.className = 'tooltiptext'; document.body.appendChild(new_item);";
    //restoring old dom once page object is created
    LOGGER.log(Level.FINER, "[FINER] js generated code " + code);
    if (this.driver instanceof JavascriptExecutor) {
      ((JavascriptExecutor) driver)
          .executeScript(code);
    }

    LOGGER.log(Level.INFO, "[DONE] Tooltip created");
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
