/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import formcrawl.FormCrawl;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import java.io.File;
import java.util.function.Function;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

/**
 *
 * @author tilak
 */
public final class Page {

	/**
	 * List of all the forms in the page
	 */
	private final ArrayList<Form> forms;
	protected final URL url_value;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        private static final String CHROME_DRIVER_PATH = "/Users/ptilak/bin/chromedriver";
	private String page_source;
	private Document soup;
	private WebDriver driver;

	/**
	 * HashMap to categorize the various Input types
	 */
	private HashMap<Input.FIELDTYPES, ArrayList<Input>> field_map;

	/**
	 * Constructor to initiate form processing
	 *
	 * @param url_value - The string representation web page's url
	 * @throws Exception - Multiple exceptions can be anticipated
	 */
	public Page(String url_value) throws Exception {
                System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		this.url_value = new URL(url_value);
		this.forms = new ArrayList<Form>();

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
		
		for(Form f : this.forms){
			
			f.submitForm();
		}
		

	}

	private void processSeleniumChoice() {
		LOGGER.log(Level.INFO, "[INFO] Starting chrome driver");
		WebDriver driver = new ChromeDriver();
		this.driver = driver;
		driver.get(this.url_value.toString());
		LOGGER.log(Level.INFO, "[INFO] Selenium waiting for js to load");
		//wait for 10 seconds before capturing HTML content
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.page_source = driver.getPageSource();

	}



	/**
	 *
	 * Creates and filter HTML DOM elements.
	 */
	private void createDOM() {
		this.page_source = this.page_source.trim().replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ");
		this.soup = Jsoup.parse(this.page_source);
		this.soup.select("script, style, head, .hidden, noscript, img, iframe, header, footer, br, code, nav").remove();
		LOGGER.log(Level.INFO, "[INFO] HTML source filtered");
	}

	private void findForms() {

		Elements found_forms = this.soup.body().getElementsByTag("FORM");
		LOGGER.log(Level.INFO, "[INFO] Found {0} forms in the page", found_forms.size());

		int i = 1;
		for (Element form : found_forms) {
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

	private void removeCSS() {
		LOGGER.log(Level.INFO, "[INFO] Removing CSS to analyze page");
		try {
			File f = new File("./js_scripts/remove_css.js");
			String code = new Scanner(f).useDelimiter("\\Z").next();
			if (this.driver instanceof JavascriptExecutor) {
				((JavascriptExecutor) driver)
					.executeScript(code);

				WebElement element = this.driver.findElement(By.cssSelector("body"));
				this.createTooltip(element, "CSS removed for analyzing page structure");

			}

		} catch (IOException e) {
			LOGGER.log(Level.INFO, "[FAIL] Error in reading remove_css.js script");
			LOGGER.log(Level.FINEST, "[ERROR] ", e);

		}

	}

	public WebDriver getDriver() {
		return this.driver;
	}

	/**
	 * Captures screenshot of element
	 *
	 * @param e
	 * @return
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


		int xcord = x;
		int ycord = y;

		BufferedImage img = ImageIO.read(screen);
		// TODO Is -2 required?
		BufferedImage dest = img.getSubimage(x, y + 2, w, h); //-2 to adjust black borders
		ImageIO.write(dest, "png", screen);
		String random_name = "./screenshots/" + "screen" + e.hashCode() + "" + (int) (Math.random() * 1000) + ".png";
		FileUtils.copyFile(screen, new File(random_name));
		LOGGER.log(Level.FINER, "[DEBUG] " + random_name);
		return random_name;

	}

	public String getTopLabelScreenshot(Input inp) throws IOException {
		LOGGER.log(Level.INFO, "[INFO] Starting top label screenshot calculation");
		Element input = inp.getElement();

		WebElement element = this.driver.findElement(By.cssSelector(input.cssSelector()));
		int ImageWidth = element.getSize().getWidth();
		int ImageHeight = element.getSize().getHeight();

		int x, y, w, h;

		Point up = element.getLocation();
		y = element.getLocation().getY() - 50; //font size is 32 px default
		x = up.getX();
		w = element.getSize().getWidth();
		h = 45;
		LOGGER.log(Level.FINEST, "[DEBUG] " + x + "" + y + " " + w + " " + h);
		return this.getElementScreenShot(input, x, y, w, h);
	}

	/**
	 * Returns page dom
	 * @return
	 */
	public Document getDom(){
		return this.soup;
	}

	/**
	 *
	 * @param inp
	 * @return
	 */
	public String getTopLabelFieldScreenshot(Input inp) throws IOException {
		LOGGER.log(Level.INFO, "[INFO] Starting top label field screenshot calculation");
		Element input = inp.getElement();

		WebElement element = this.driver.findElement(By.cssSelector(input.cssSelector()));
		int ImageWidth = element.getSize().getWidth();
		int ImageHeight = element.getSize().getHeight();

		int x, y, w, h;

		Point up = element.getLocation();
		y = element.getLocation().getY() - 55; //font size is 32 px default
		x = up.getX() - 5;
		w = element.getSize().getWidth() + 10;
		h = 95;

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

	public void createTooltip(WebElement element, String text) {
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
