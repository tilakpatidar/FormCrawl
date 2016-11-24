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
    public static enum FIELDTYPES { TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT,  FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, UNDEFINED_INPUT, RESET_BUTTON, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT, SUBMIT_INPUT };
    protected final Input.FIELDTYPES INPUT_TYPE;
    protected String input_title;
    private final Form form;
    
    public Input(Form f, Element ip, Input.FIELDTYPES field_type){
        this.form = f;
        this.INPUT_TYPE = field_type;
               
    }
    
    
   
    
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }
}
