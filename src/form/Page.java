/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;
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
    private static final Logger LOGGER;
    private String page_source;
    private Document soup;
    private WebDriver driver;

    public static enum HANDLE_TYPES { PLAIN_HTML, SELENIUM};

    private final Page.HANDLE_TYPES form_choice;

    /**
     * HashMap to categorize the various Input types
     */
    private HashMap<Input.FIELDTYPES, ArrayList<Input>> field_map;
    /**
     * Constructor to initiate form processing
     * @param url_value - The string representation web page's url
     * @param ordinal - Index of enum HANDLE_TYPES
     * @throws Exception - Multiple exceptions can be anticipated
     */
    public Page(String url_value, int ordinal) throws Exception{
        this.url_value = new URL(url_value);
        this.forms = new ArrayList<Form>();

        System.out.println(this.toString());
	    switch (ordinal) {
	    	    case 0:
			    //Plain HTML selected
			    this.form_choice = Page.HANDLE_TYPES.PLAIN_HTML;
			    this.processHTMLChoice();
			    this.filterHTML();
			    this.createDOM();
			    this.findForms();
			    break;
	    	    case 1:
			    //Selenium processing selected
			    
			    this.form_choice = Page.HANDLE_TYPES.SELENIUM;
			    this.processSeleniumChoice();
			    this.removeCSS();
			    this.filterHTML();
			    this.createDOM();
			    this.findForms();
			    break;
	    	    default:
			    LOGGER.info("Invalid selection value in form processing type");
			    LOGGER.log(Level.FINEST, "Invalid selection value for form processing type: {0}", ordinal);
			    throw new RuntimeException("Invalid selection value for form processing type: " +  ordinal);
	    }




    }

    static{
        LOGGER = Logger.getGlobal();
    }

    private void processHTMLChoice(){

        try{
            URLConnection uc = this.url_value.openConnection();
            InputStream ip = uc.getInputStream();
            LOGGER.info("Reading web page");
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(ip, "UTF-8"))) {
                String inputLine;
                StringBuilder a = new StringBuilder();
                while ((inputLine = buff.readLine()) != null){
                    a.append(inputLine);
                }
                this.page_source = a.toString();
                LOGGER.log(Level.INFO, "Web page read {0} chars", a.length());

            }catch(Exception e){
                LOGGER.info("Error in reading web page");
                LOGGER.log(Level.FINEST, "[ERROR] {0}", e.getMessage());
            }

        }catch(MalformedURLException e){
            LOGGER.info("[ERROR] URL error");
            LOGGER.log(Level.FINEST, "[ERROR] {0}", e.getMessage());

        }catch (IOException e) {
            LOGGER.info("[ERROR] Download error");
            LOGGER.log(Level.FINEST, "[ERROR] {0}", e.getMessage());
        }finally{


        }

    }


    private void processSeleniumChoice() {
         WebDriver driver  = new ChromeDriver();
         this.driver = driver;
         driver.get(this.url_value.toString());

         //wait for 10 seconds before capturing HTML content
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
         this.page_source = driver.getPageSource();

    }
    /**
     * Filters the HTML content.
     *
     */
    private void filterHTML(){
        this.page_source = this.page_source.trim().replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ");
    }

    /**
     *
     * Creates and filter HTML DOM elements.
     */
    private void createDOM(){
        this.soup = Jsoup.parse(this.page_source);
        this.soup.select("script, style, head, .hidden, noscript, img, iframe, header, footer, br, code, nav").remove();
    }


    private void findForms(){

        Elements found_forms = this.soup.body().getElementsByTag("FORM");
        LOGGER.log(Level.INFO, "[INFO] Found {0} forms in the page", found_forms.size());

        int i = 1;
        for (Element form : found_forms) {
            //create form objects
            try{
                LOGGER.log(Level.INFO, "[INFO] creating form {0}", i);
                Form f = new Form(this, form);
                this.forms.add(f);

            }catch(Exception e){
                LOGGER.log(Level.INFO, "[ERROR] Error in creating form {0}", i);
            }finally{
                i++;
            }

        }

    }

    private void removeCSS(){

      if(this.form_choice == Page.HANDLE_TYPES.PLAIN_HTML){
        //if plain html selected do not execute the rest
        return;
      }

      try{
        File f = new File("./js_scripts/remove_css.js");
        String code = new Scanner(f).useDelimiter("\\Z").next();
        if (this.driver instanceof JavascriptExecutor) {
          System.out.println("LOL");
          System.out.println(code);
      		((JavascriptExecutor) driver)
      			.executeScript(code);
      	}

      }catch(IOException e){
        e.printStackTrace();
        LOGGER.log(Level.FINEST, "[ERROR] Error in reading remove_css.js script {0}", e.getMessage());
        LOGGER.log(Level.INFO, "[ERROR] Error in reading remove_css.js script");
      }


    }
    public String toString(){
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }


}
