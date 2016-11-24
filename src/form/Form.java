/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jsoup.nodes.Element;

/**
 * Represents a form instance with many inputs and actions.
 * @author tilak
 */
public class Form {
    
    private static enum METHODS {GET, POST, PUT, DELETE};
    private final URL form_action;
    private final Page page;
    private final Form.METHODS form_method;
    /**
     * Accepts JSOUP form element
     * @param form_dom - JSOUP form element
     */
    public Form(Page page, Element form_dom){
        
        LOGGER.info("[INFO] Creating instance of Form");
        //check for type before construction
        if(!form_dom.tagName().equalsIgnoreCase("FORM")){
            LOGGER.info("[ERROR] form_dom must be of tagName type \"form\"");
            throw new RuntimeException("form_dom must be of tagName type \"form\"");
        }
        
        
        
        String method = form_dom.attr("method").toUpperCase();
        switch(method){
            case "GET":
                this.form_method = Form.METHODS.GET;
                break;
            case "POST":
                this.form_method = Form.METHODS.POST;
                break;
            case "PUT":
                this.form_method = Form.METHODS.PUT;
                break;               
            case "DELETE":
                this.form_method = Form.METHODS.DELETE;
                break;
            default:
                //no matching method
                this.form_method = Form.METHODS.GET;
                //The default method when submitting form data is GET. (from w3schools)
        
        }
        
        try {
            URI form_action = new URI(form_dom.attr("action"));
            if(form_action.toString().isEmpty()){
                LOGGER.info("[ERROR] No form action url present in form_dom");
                throw new MalformedURLException("URL empty or null");
            }
            
            if(! form_action.isAbsolute()){
                //make absolute
                this.form_action = new URL(page.url_value, form_action.toString());
            }else{
                this.form_action = form_action.toURL();
            }
        } catch (MalformedURLException ex) {
            LOGGER.info("[ERROR] FORM action URL malformed");
            LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
            throw new RuntimeException("Cannot create form object");
        } catch (URISyntaxException ex) {
            LOGGER.info("[ERROR] FORM action URI cast malformed");
            LOGGER.log(Level.FINEST, "[ERROR] {0}", ex.getMessage());
            throw new RuntimeException("Cannot create form object");
        }
        
        

        
        this.page = page;
       
        
        LOGGER.info("[INFO] Form action and method detected");
        
    }
    
    
    
    //for logging in class Form
    private static final Logger LOGGER;
    static{
        LOGGER = Logger.getGlobal();
    }
    
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }

    
}
