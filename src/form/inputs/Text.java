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
public class Text extends Input {

    public Text(Form f, Element ip, Input.FIELDTYPES field_type) {
        super(f, ip, field_type);
    }
    
}
