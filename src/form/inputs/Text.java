/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form.inputs;

import form.Form;
import form.Input;
import org.jsoup.nodes.Element;

/**
 * Text input implementation
 * @author tilak
 */
public class Text extends Input implements InputRequirement {

    public Text(Form f, Element ip, Input.FIELDTYPES field_type) {
        super(f, ip, field_type);
    }

    @Override
    public FIELDTYPES getType() {
        return super.INPUT_TYPE;
    }

    @Override
    public void fill() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        if(super.input_title == null || super.input_title.isEmpty()){
            //extract title
          
        }else{
            //return extracted title
            return super.input_title;
        }
        return null;
    }

    @Override
    public String getCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
