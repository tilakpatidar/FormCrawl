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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
/**
 *
 * @author tilak
 */
public class Form {
    
    /**
     * List of all the inputs in the Form
     */
    private ArrayList<Input> inputs;
    private final String url_value;
    private static final Logger LOGGER;

    public static enum HANDLE_TYPES { PLAIN_HTML, SELENIUM};
    
    private final Form.HANDLE_TYPES form_choice;
    
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
    public Form(String url_value, int ordinal) throws Exception{
        this.url_value = url_value;
        
        if(ordinal == 0){
                //Plain HTML selected
                this.form_choice = Form.HANDLE_TYPES.PLAIN_HTML;
                this.processHTMLChoice();
        }else if(ordinal == 1){
                //Selenium processing selected
                this.form_choice = Form.HANDLE_TYPES.SELENIUM;
                this.processSeleniumChoice();
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
            URL url = new URL(url_value);
            URLConnection uc = url.openConnection();
            InputStream ip = uc.getInputStream();
            LOGGER.info("Reading web page");
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(ip, "UTF-8"))) {
                String inputLine;
                StringBuilder a = new StringBuilder();
                while ((inputLine = buff.readLine()) != null){
                    a.append(inputLine);
                }
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
         driver.get(this.url_value);
         driver.quit();
    }
    
    
}
