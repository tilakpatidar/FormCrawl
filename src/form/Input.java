/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * abstract class for all types of inputs.
 * @author tilak
 */
public abstract class Input {
    /**
     *
     * Tells us the various types of field types available in the HTML form
     */
    public static enum FIELDTYPES { TEXTAREA_INPUT, TEXT_INPUT, SELECT_INPUT, CHECKBOX_INPUT, RADIO_INPUT,  FILE_INPUT, BUTTON_INPUT, COLOR_INPUT, DATE_INPUT, EMAIL_INPUT, IMAGE_INPUT, NUMBER_INPUT, PASSWORD_INPUT, HIDDEN_INPUT, UNDEFINED_INPUT, RESET_BUTTON, SEARCH_INPUT, TELEPHONE_INPUT, TIME_INPUT, URL_INPUT, WEEK_INPUT, SUBMIT_INPUT };


    public abstract Form getAssociatedForm();

    /**
     * For getting the FIELD_TYPE of any Input IS-A Object
     * @return
     */
    public abstract Input.FIELDTYPES getType();
    /**
     *
     * Fill the Input IS-A object with some value
     */
    public abstract void fill();

    /**
     * Get the label of the Input
     * @return
     */
    public abstract String getTitle();

    /**
     *
     * For getting the category of the input for suggestion engine
     * @return
     */
    public abstract String getCategory();

    /**
    * Replaces common label punctuations used in the field title
    * @return String
    */
    public static String filter_label(String text){
      text = text.replaceAll("\\*|\\:|\\-"," ").replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " ").replaceAll("\\s+", " ");
      return text.trim();
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }
}
