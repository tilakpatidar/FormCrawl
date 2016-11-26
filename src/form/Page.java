/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
/**
 *
 * @author tilak
 */
public class Page {

    /**
     * List of all the forms in the page
     */
    private ArrayList<Form> forms;
    protected final URL url_value;
    private static final Logger LOGGER;
    private String page_source;
    private Document soup;

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

        if(ordinal == 0){
                //Plain HTML selected
                this.form_choice = Page.HANDLE_TYPES.PLAIN_HTML;
                this.processHTMLChoice();
                this.filterHTML();
                this.createDOM();
                this.findForms();
        }else if(ordinal == 1){
                //Selenium processing selected
                this.form_choice = Page.HANDLE_TYPES.SELENIUM;
                this.processSeleniumChoice();
                this.filterHTML();
                this.createDOM();
                this.findForms();
        }else{
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
        this.driver.execute_script = "
        setInterval(function(){
          var toRemove=[];
          toRemove.push.apply(toRemove, document.querySelectorAll('link[type*="css"]'));
          toRemove.push.apply(toRemove, document.querySelectorAll('style'));
          toRemove.push.apply(toRemove, document.querySelectorAll('img'));
          toRemove.push.apply(toRemove, document.querySelectorAll('canvas'));
          toRemove.forEach(function(s){
            s.parentNode.removeChild(s);
          });

          [].forEach.call(document.querySelectorAll('[style]'), function(e){

            e.removeAttribute('style');
          });

          var stylesheets = document.styleSheets;
          var len = stylesheets.length;
          while(len!=0){
            var sh = stylesheets[len - 1];

            var count = sh.rules.length;

            while(count!=0)  {
              sh.deleteRule(0);
              count = sh.rules.length;
            }
            len--;
          }

          var km = ['click', 'dblclick', 'mousedown', 'mousemove', 'mouseover', 'mouseout', 'mouseup', 'mouseenter', 'mouseleave', 'keydown', 'keypress', 'keyup', 'scroll'];
          function preventAll(parent){
            var dom = parent.getElementsByTagName('*');
            for(var i=0,l=dom.length; i<l; i++){
              for(var n=0,c=km.length; n<c; n++){
                dom[i]['on'+km[n]] = function(e){
                  e = e || event;
                  e.preventDefault();
                  return false;
                }
              }
            }
            var fr = frames;
            for(var i=0,l=fr.length; i<l; i++){
              // cancell frames events here
            }
          }
          preventAll(document);


          for(var n=0,c=km.length; n<c; n++){
            window['on'+km[n]] = function(e){
              e = e || event;
              e.preventDefault();
              return false;
            }
          }

        }, 1000);
      ";

    }
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }


}
