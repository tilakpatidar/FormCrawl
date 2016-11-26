/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form.inputs;

import form.Form;
import form.Input;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * Text input implementation
 * @author tilak
 */
public class Text extends Input implements InputRequirement {
    private final Element text_input;
    
    public Text(Form f, Element ip, Input.FIELDTYPES field_type) {
        super(f, ip, field_type);
        this.text_input = ip;
        this.getTitle();
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
            
            //case 1 : if label is next or prev of input
            Element next = this.text_input.nextElementSibling();
            Element prev = this.text_input.previousElementSibling();
            System.out.println("hey");
            System.out.println(next);
            System.out.println(prev);
            if(next.tagName().equals("label")){
                this.input_title = next.text();
            }else if(prev.tagName().equals("label")){
                this.input_title = prev.text();
            }
          
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
