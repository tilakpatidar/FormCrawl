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
 * Text input implementation
 *
 * @author tilak
 */
public class Text extends Input {

	private final Element text_input;
	private final Form form;
	private final Input.FIELDTYPES INPUT_TYPE;
	private final String input_title;
	private final String placeholder;
	private Input.ORIENTATIONS input_orientation;
	private final boolean required;

	public Text(Form f, Element ip, Input.FIELDTYPES field_type) throws IOException {
		this.INPUT_TYPE = field_type;
		this.text_input = ip;
		this.form = f;
		//detect input_orientation
		this.input_orientation = Input.detectOrientation(this);
		this.input_title = Input.findLabel(this);
		this.placeholder = Input.findPlaceHolder(this);
		this.required = Input.findRequired(this.text_input);
		
	}

	@Override
	public Form getAssociatedForm() {
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
	public String getCategory() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Element getElement() {
		return this.text_input;
	}

	@Override
	public String getTitle() {
		return this.input_title;
	}

	@Override
	public String getPlaceHolder() {
		return this.placeholder;
	}

	@Override
	public ORIENTATIONS getOrientation() {
		return this.input_orientation;
	}

	@Override
	public boolean isRequired() {
		return this.required;
	}

}
