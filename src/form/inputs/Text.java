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
public class Text extends Input {

    private final Element text_input;
    private final Form form;
    private final Input.FIELDTYPES INPUT_TYPE;
    private final String input_title;

    public Text(Form f, Element ip, Input.FIELDTYPES field_type) {
        this.INPUT_TYPE = field_type;
        this.text_input = ip;
        this.form = f;
        //extract and set title
        this.input_title = this.getTitle();

    }


    @Override
    public Form getAssociatedForm(){
      return this.form;
    }

    @Override
    public FIELDTYPES getType() {
        return this.INPUT_TYPE;
    }

    @Override
    public void fill() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        if(this.input_title == null || this.input_title.isEmpty()){
            //extract title
            //case 1 : Try to see if all inputs have their respective different parents, not single parents
            Element parent = this.text_input.parent();
            if(Form.detectFields(parent).size() == 1){
              //it's parent have only one input
              //now try if it's parent's parent's have multiple inputs
              if(Form.detectFields(parent.parent()).size() > 1){
                //yes it has div by div structure
                return Input.filter_label(parent.text());
              }
            }
            //case 1 : if label is next or prev of input
            Element next = this.text_input.nextElementSibling();
            Element prev = this.text_input.previousElementSibling();
            System.out.println("hey");
            System.out.println(next);
            System.out.println(prev);
            if(next.tagName().equals("label")){
                return next.text();
            }else if(prev.tagName().equals("label")){
                return prev.text();
            }

        }else{
            //return extracted title
            return this.input_title;
        }
        return null;
    }

    @Override
    public String getCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
