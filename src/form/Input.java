/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jsoup.nodes.Element;

/**
 * Super class for all types of inputs.
 * @author tilak
 */
public class Input {
    /**
     * 
     * Tells us the various types of field types available in the HTML form
     */
    public static enum FIELDTYPES { TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT,  FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, UNDEFINED_INPUT, RESET_BUTTON, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT };
    private final Input.FIELDTYPES INPUT_TYPE;
    private final Form form;
    
    public Input(Form f, Element ip){
        this.form = f;
        String tag_name = ip.tagName();
        switch(tag_name){
            case "input":
                String input_type = ip.attr("type").toLowerCase();

                switch(input_type){
                    case "text":
                        this.INPUT_TYPE = Input.FIELDTYPES.TEXT_INPUT;
                        break;
                    case "radio":
                        this.INPUT_TYPE = Input.FIELDTYPES.RADIO_INPUT;
                        break;
                    case "file":
                        this.INPUT_TYPE = Input.FIELDTYPES.FILE_INPUT;
                        break;
                    case "checkbox":
                        this.INPUT_TYPE = Input.FIELDTYPES.CHECKBOX_INPUT;
                        break;
                    case "button":
                        this.INPUT_TYPE = Input.FIELDTYPES.BUTTON_INPUT;
                        break;
                    case "color":
                        this.INPUT_TYPE = Input.FIELDTYPES.COLOR_INPUT;
                        break;
                    case "date":
                        this.INPUT_TYPE = Input.FIELDTYPES.DATE_INPUT;
                        break;   
                    case "email":
                        this.INPUT_TYPE = Input.FIELDTYPES.EMAIL_INPUT;
                        break; 
                    case "hidden":
                        this.INPUT_TYPE = Input.FIELDTYPES.HIDDEN_INPUT;
                        break;   
                    case "image":
                        this.INPUT_TYPE = Input.FIELDTYPES.IMAGE_INPUT;
                        break;  
                    case "number":
                        this.INPUT_TYPE = Input.FIELDTYPES.NUMBER_INPUT;
                        break; 
                    case "password":
                        this.INPUT_TYPE = Input.FIELDTYPES.PASSWORD_INPUT;
                        break; 
                    case "reset":
                        this.INPUT_TYPE = Input.FIELDTYPES.RESET_BUTTON;
                        break;
                    case "search":
                        this.INPUT_TYPE = Input.FIELDTYPES.SEARCH_INPUT;
                        break;
                    case "submit":
                        this.INPUT_TYPE = Input.FIELDTYPES.SEARCH_INPUT;
                        break;
                    case "tel":
                        this.INPUT_TYPE = Input.FIELDTYPES.TELEPHONE_INPUT;
                        break;
                    case "time":
                        this.INPUT_TYPE = Input.FIELDTYPES.TIME_INPUT;
                        break;
                    case "url":
                        this.INPUT_TYPE = Input.FIELDTYPES.URL_INPUT;
                        break;         
                    case "week":
                        this.INPUT_TYPE = Input.FIELDTYPES.WEEK_INPUT;
                        break; 
                    default:
                      this.INPUT_TYPE = Input.FIELDTYPES.UNDEFINED_INPUT;
                      throw new RuntimeException("Undefined input detected");           
                }
            break;
                
            case "select":
                this.INPUT_TYPE = Input.FIELDTYPES.SELECT_INPUT;
                break;
            case "button":
                this.INPUT_TYPE = Input.FIELDTYPES.BUTTON_INPUT;
                break;
            case "textarea":
                this.INPUT_TYPE = Input.FIELDTYPES.TEXTAREA_INPUT;
                break;
            default:
                this.INPUT_TYPE = Input.FIELDTYPES.UNDEFINED_INPUT;
                throw new RuntimeException("Undefined input detected");
        }
        
    }
    
    
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }
}
