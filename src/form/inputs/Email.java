/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import java.io.IOException;
import org.jsoup.nodes.Element;
/**
 * Email input implementation
 *
 * @author tilak
 */
public class Email extends Input {

	public Email(Form f, Element ip, Input.FIELDTYPES field_type) throws IOException, Exception {
		super(f, ip, field_type);
	}

	@Override
	public void fill() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
