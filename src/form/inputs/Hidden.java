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
 * Hidden input implementation
 *
 * @author tilak
 */
public class Hidden extends Input {

	public Hidden(Form f, Element ip) throws IOException, Exception {
		super(f, ip, Input.FIELDTYPES.HIDDEN_INPUT);
	}

	@Override
	public void fill(String val) throws UnsupportedOperationException{
		
		throw new UnsupportedOperationException("Hidden inputs cannot be filled");
		
	}
	
	@Override
	public String getTitle() {
		return this.getName();
	}

	/**
	 * Returns placeholder for element
	 *
	 * @return
	 */
	@Override
	public String getPlaceHolder() {
		return this.getName();
	}

	@Override
	public ORIENTATIONS getOrientation() {
		return null;
	}

	@Override
	public boolean isRequired() {
		return true;
	}


}

